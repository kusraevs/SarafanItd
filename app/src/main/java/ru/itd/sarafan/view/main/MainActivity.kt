package ru.itd.sarafan.view.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding2.widget.RxAdapterView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import ru.itd.sarafan.R
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.businesslogic.interactors.ChangeChildCategoryInteractor
import ru.itd.sarafan.businesslogic.interactors.ChangeRootCategoryInteractor
import ru.itd.sarafan.businesslogic.interactors.GetCategoriesInteractor
import ru.itd.sarafan.businesslogic.interactors.GetWeekTypeInteractor
import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.Category
import ru.itd.sarafan.utils.FragmentUtils
import ru.itd.sarafan.utils.RouterUtils
import ru.itd.sarafan.view.adapter.CategoriesSpinnerAdapter
import ru.itd.sarafan.view.drawer.RootCategoriesAdapter
import ru.itd.sarafan.view.posts.PostsActivity
import ru.itd.sarafan.view.posts.PostsFragment
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : MviActivity<MainView, MainPresenter>(), RootCategoriesAdapter.RootCategoryClickListener, MainView {

    @BindView(R.id.rv_root_categories) lateinit var rvRootCategories: RecyclerView
    @BindView(R.id.categories_spinner) lateinit var categoriesSpinner: AppCompatSpinner
    @BindView(R.id.search_view) lateinit var searchView: SearchView
    @BindView(R.id.nav_view) lateinit var navView: NavigationView
    lateinit var tvIsDenominator: TextView

    private val rootCategoryClickSubject = PublishSubject.create<Category>()
    private val searchQuerySubmittedSubject = PublishSubject.create<String>()

    private lateinit var rootCategoriesAdapter: RootCategoriesAdapter
    private lateinit var categoriesSpinnerAdapter: CategoriesSpinnerAdapter

    private lateinit var presenter: MainPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        tvIsDenominator = navView.getHeaderView(0).findViewById(R.id.tv_is_denominator)
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
        presenter = MainPresenter(GetCategoriesInteractor(categories), ChangeRootCategoryInteractor(),
                ChangeChildCategoryInteractor(), GetWeekTypeInteractor())
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

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun startLoadingIntent(): Observable<Boolean> = Observable.just(true)
    override fun getCurrentWeekTypeIntent(): Observable<Boolean> = Observable.just(true)

    override fun rootCategorySelected(): Observable<Category> = rootCategoryClickSubject
    override fun childCategorySelected(): Observable<Category> =
            RxAdapterView.itemSelections(categoriesSpinner)
                    .skipInitialValue()
                    .map { position -> categoriesSpinnerAdapter.getItem(position) }
                    .distinctUntilChanged()



    override fun render(state: MainViewState) {
        if (state.data != null)
            showRootCategories(state.data.categories, state.rootCategory)

        state.rootCategory?.childs?.let {
            showChildCategories(it, state.childCategory)
        }

        if (state.navigateToSearchWithQuery != ""){
            val intent = Intent(this, PostsActivity::class.java)
            intent.putExtra(PostsFragment.ARG_SEARCH_QUERY, state.navigateToSearchWithQuery)
            startActivity(intent)
            searchQuerySubmittedSubject.onNext("")
        }

        if (state.isDenominator != null) {
            val typeStr = getString(
                    if (state.isDenominator)
                        R.string.denominator_type
                    else
                        R.string.numerator_type
            )
            tvIsDenominator.text = typeStr
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

    private fun showChildCategories(childCategories: List<Category>, selectedCategory: Category?) {
        categoriesSpinnerAdapter.clear()
        categoriesSpinnerAdapter.addAll(childCategories)
        categoriesSpinnerAdapter.notifyDataSetChanged()
        val selectedIndex = childCategories.indexOf(selectedCategory)
        if (selectedIndex != -1)
            categoriesSpinner.setSelection(selectedIndex)
    }


}
