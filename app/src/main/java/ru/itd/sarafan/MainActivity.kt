package ru.itd.sarafan

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.AdapterView
import butterknife.BindView
import butterknife.ButterKnife
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import ru.itd.sarafan.rest.interactors.ChangeChildCategoryInteractor
import ru.itd.sarafan.rest.interactors.ChangeRootCategoryInteractor
import ru.itd.sarafan.rest.interactors.GetCategoriesInteractor
import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.Category
import ru.itd.sarafan.utils.FragmentUtils
import ru.itd.sarafan.utils.RouterUtils
import ru.itd.sarafan.view.adapter.CategoriesSpinnerAdapter
import ru.itd.sarafan.view.drawer.RootCategoriesAdapter
import ru.itd.sarafan.view.main.MainPresenter
import ru.itd.sarafan.view.main.MainView
import ru.itd.sarafan.view.main.MainViewState
import ru.itd.sarafan.view.posts.PostsActivity
import ru.itd.sarafan.view.posts.PostsFragment
import ru.itd.sarafan.view.posts.PostsPresenter
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : MviActivity<MainView, MainPresenter>(), RootCategoriesAdapter.RootCategoryClickListener,
        PostsPresenter.MainPresenterHolder, MainView {

    @BindView(R.id.rv_root_categories) lateinit var rvRootCategories: RecyclerView
    @BindView(R.id.categories_spinner) lateinit var categoriesSpinner: AppCompatSpinner
    @BindView(R.id.search_view) lateinit var searchView: SearchView

    private val rootCategoryClickSubject = PublishSubject.create<Category>()
    private val childCategoryClickSubject = PublishSubject.create<Category>()
    private val searchQuerySubmittedSubject = PublishSubject.create<String>()

    private lateinit var rootCategoriesAdapter: RootCategoriesAdapter
    private lateinit var categoriesSpinnerAdapter: CategoriesSpinnerAdapter

    private lateinit var presenter: MainPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        SarafanApplication.getComponent().inject(this)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        setUpRecyclerViews()
        //nav_view.setNavigationItemSelectedListener(this)
        FragmentUtils.replaceFragment(supportFragmentManager, PostsFragment(), FragmentUtils.POST_FRAGMENT_TAG)

        RxSearchView.queryTextChangeEvents(searchView)
                .filter { event -> event.isSubmitted }
                .map { event -> event.queryText().toString() }
                .subscribe(searchQuerySubmittedSubject::onNext)

    }

    override fun searchTextSubmitted(): Observable<String> = searchQuerySubmittedSubject


    override fun createPresenter(): MainPresenter {
        val categories = intent.getSerializableExtra(RouterUtils.CATEGORIES_INTENT_KEY) as Categories
        presenter = MainPresenter(GetCategoriesInteractor(categories), ChangeRootCategoryInteractor(), ChangeChildCategoryInteractor())
        return presenter
    }

    private fun setUpRecyclerViews(){
        rvRootCategories.layoutManager = LinearLayoutManager(applicationContext)
        rootCategoriesAdapter = RootCategoriesAdapter(applicationContext, Collections.emptyList())
        rvRootCategories.adapter = rootCategoriesAdapter
        rootCategoriesAdapter.clickListener = this

        categoriesSpinnerAdapter = CategoriesSpinnerAdapter(applicationContext, ArrayList<Category>())
        categoriesSpinner.adapter = categoriesSpinnerAdapter
    }

    override fun onRootCategoryClicked(category: Category) {
        drawer_layout.closeDrawer(GravityCompat.START)
        rootCategoryClickSubject.onNext(category)
    }

    override fun getMainPresenter(): MainPresenter = presenter


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun startLoadingObservable(): Observable<Boolean> = Observable.just(true)
    override fun rootCategorySelected(): Observable<Category> = rootCategoryClickSubject
    override fun childCategorySelected(): Observable<Category> = childCategoryClickSubject


    override fun render(state: MainViewState) {
        if (state.data != null)
            showRootCategories(state.data.categories, state.rootCategory)

        state.rootCategory?.childs?.let {
            showChildCategories(it)
        }

        if (state.navigateToSearchWithQuery != ""){
            val intent = Intent(this, PostsActivity::class.java)
            intent.putExtra(PostsFragment.ARG_SEARCH_QUERY, state.navigateToSearchWithQuery)
            startActivity(intent)
            searchQuerySubmittedSubject.onNext("")
        }
    }


    private fun showRootCategories(rootCategories: List<Category>, rootCategory: Category?) {
        rootCategoriesAdapter.data = rootCategories
        rootCategoriesAdapter.selectedCategory = rootCategory
        rvRootCategories.adapter.notifyDataSetChanged()

        //TODO Temporary solution with delay
        if (rootCategories.isNotEmpty() && rootCategory == null)
            Handler().postDelayed({ rootCategoryClickSubject.onNext(rootCategories[0]) }, 100)
    }

    private fun showChildCategories(childCategories: List<Category>) {
        categoriesSpinnerAdapter.clear()
        categoriesSpinnerAdapter.addAll(childCategories)
        categoriesSpinnerAdapter.notifyDataSetChanged()
        categoriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                childCategoryClickSubject.onNext(childCategories[position])
            }

        }

    }


}
