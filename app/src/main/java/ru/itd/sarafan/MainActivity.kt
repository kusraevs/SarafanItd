package ru.itd.sarafan

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import butterknife.BindView
import butterknife.ButterKnife
import com.hannesdorfmann.mosby3.mvi.MviActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import ru.itd.sarafan.rest.CategoriesLoader
import ru.itd.sarafan.rest.interactors.ChangeChildCategoryInteractor
import ru.itd.sarafan.rest.interactors.ChangeRootCategoryInteractor
import ru.itd.sarafan.rest.interactors.GetCategoriesInteractor
import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.Category
import ru.itd.sarafan.utils.FragmentUtils
import ru.itd.sarafan.utils.IntentUtils
import ru.itd.sarafan.view.adapter.CategoriesSpinnerAdapter
import ru.itd.sarafan.view.drawer.RootCategoriesAdapter
import ru.itd.sarafan.view.main.MainPresenter
import ru.itd.sarafan.view.main.MainView
import ru.itd.sarafan.view.main.MainViewState
import ru.itd.sarafan.view.posts.PostsFragment
import ru.itd.sarafan.view.posts.PostsPresenter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainActivity : MviActivity<MainView, MainPresenter>(), RootCategoriesAdapter.RootCategoryClickListener,
        PostsPresenter.MainPresenterHolder, MainView {

    @BindView(R.id.rv_root_categories) lateinit var rvRootCategories: RecyclerView
    @BindView(R.id.categories_spinner) lateinit var categoriesSpinner: AppCompatSpinner

    private val rootCategoryClickSubject = PublishSubject.create<Category>()
    private val childCategoryClickSubject = PublishSubject.create<Category>()

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


    }

    override fun createPresenter(): MainPresenter {
        val categories = intent.getSerializableExtra(IntentUtils.CATEGORIES_INTENT_KEY) as Categories
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // post you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun startLoadingObservable(): Observable<Boolean> = Observable.just(true)
    override fun rootCategorySelected(): Observable<Category> = rootCategoryClickSubject
    override fun childCategorySelected(): Observable<Category> = childCategoryClickSubject


    override fun render(state: MainViewState) {
        if (state.data != null)
            showRootCategories(state.data.categories!!, state.rootCategory)
        if (state.rootCategory != null) {
            showChildCategories(state.rootCategory.childs!!)
        }
    }


    private fun showRootCategories(rootCategories: List<Category>, rootCategory: Category?) {
        rootCategoriesAdapter.data = rootCategories
        rootCategoriesAdapter.selectedCategory = rootCategory
        rvRootCategories.adapter.notifyDataSetChanged()
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
