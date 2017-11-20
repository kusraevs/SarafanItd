package ru.itd.sarafan.view.posts

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.businesslogic.ActivatedCategoriesManager
import ru.itd.sarafan.rest.interactors.GetCategoriesInteractor
import ru.itd.sarafan.rest.interactors.GetSearchQueryInteractor
import ru.itd.sarafan.rest.interactors.GetTagInteractor
import ru.itd.sarafan.rest.interactors.LoadPostsInteractor
import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.tags.Term
import ru.itd.sarafan.view.main.MainPresenter
import java.util.*
import javax.inject.Inject

/**
 * Created by macbook on 16.10.17.
 */
class PostsPresenter(private val getTagInteractor: GetTagInteractor,
                     private val getCategoriesInteractor: GetCategoriesInteractor,
                     private val getSearchQueryInteractor: GetSearchQueryInteractor) : MviBasePresenter<PostsView, PostsViewState>() {

    @Inject lateinit var loadPosts: LoadPostsInteractor
    @Inject lateinit var activatedCategoriesManager: ActivatedCategoriesManager


    private var categories: Categories? = null
    private var tag: Term? = null
    private var searchQuery: String? = null
    private var isLoading: Boolean = false
    private var hasMore: Boolean = true

    init {
        SarafanApplication.getComponent().inject(this)
    }

    override fun bindIntents() {

        //val loadFirstPageObservable = intent(PostsView::loadFirstPageIntent)

        val initTagObservable = intent(PostsView::startInitIntent)
                .flatMap { getTagInteractor.execute() }
                .doOnNext { tag = it }
        val initCategoriesObservable = intent(PostsView::startInitIntent)
                .flatMap { getCategoriesInteractor.execute() }
                .doOnNext { this.categories = it }
        val initSearchQueryObservable = intent(PostsView::startInitIntent)
                .flatMap { getSearchQueryInteractor.execute() }
                .doOnNext { this.searchQuery = it }

        val initObservable = Observable.merge(initCategoriesObservable, initTagObservable, initSearchQueryObservable).take(1)

        val categoriesChangedObservable = activatedCategoriesManager.subscribeToUpdates()
                .doOnNext { this.categories = it }

        val loadFirstPageObservable = Observable.merge(initObservable, categoriesChangedObservable)
                .switchMap { loadPosts.loadFirstPage(tagId = tag?.id, categories = categories, searchQuery = searchQuery) }



        val loadNextPageObservable = intent(PostsView::loadNextPageIntent)
        val allLoadPagesObservable = loadNextPageObservable
                .filter { !isLoading }
                .filter { hasMore }
                .flatMap { loadPosts.loadNextPage(tagId = tag?.id, categories = categories, searchQuery = searchQuery) }


        val allIntentsObservable = Observable.merge(allLoadPagesObservable, loadFirstPageObservable)
                .scan(PostsViewState(), this::viewStateReducer)
                .doOnNext {
                    isLoading = it.loading
                    hasMore = it.hasMore
                }

        subscribeViewState(allIntentsObservable, PostsView::render)
    }


    private fun viewStateReducer(state: PostsViewState, changes: PartialPostsChanges): PostsViewState {
        return when (changes) {
            is PartialPostsChanges.NextPageLoaded -> {
                val newPosts = state.data + changes.posts
                state.copy(data = newPosts, hasMore = changes.hasMore, loading = false)
            }
            is PartialPostsChanges.FirstPageLoaded -> {
                state.copy(data = changes.posts, hasMore = changes.hasMore, loading = false)
            }
            /*
            is PartialPostsChanges.CategoriesUpdate -> {
                state.copy(categories = changes.categories.categories)
            }*/
            is PartialPostsChanges.FirstPageLoading -> state.copy(data = Collections.emptyList(), hasMore = true, error = null)

            is PartialPostsChanges.NextPageLoading -> {
                state.copy(hasMore = true, loading = true, error = null)
            }
            is PartialPostsChanges.PostsLoadingError -> {
                state.copy(hasMore = false, loading = false, error = changes.throwable)
            }
        }
    }

    private fun postsFilterReducer(filter: PostsFilter, change: PostsFilterChange): PostsFilter {
        return when (change){
            is PostsFilterChange.CategoriesChange -> filter.copy(categories = change.categories)
            is PostsFilterChange.TagChange -> filter.copy(tag = change.tagTerm)
            is PostsFilterChange.SearchQueryChange -> filter
        }
    }

    interface MainPresenterHolder {
        fun getMainPresenter(): MainPresenter
    }

}