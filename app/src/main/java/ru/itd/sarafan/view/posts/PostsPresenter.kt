package ru.itd.sarafan.view.posts

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.businesslogic.ActivatedCategoriesManager
import ru.itd.sarafan.rest.interactors.GetCategoriesInteractor
import ru.itd.sarafan.rest.interactors.GetSearchQueryInteractor
import ru.itd.sarafan.rest.interactors.GetTagInteractor
import ru.itd.sarafan.rest.interactors.LoadPostsInteractor
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


    private var isLoading: Boolean = false
    private var hasMore: Boolean = true

    init {
        SarafanApplication.getComponent().inject(this)
    }

    override fun bindIntents() {

        val stateChangeObservable = Observable.combineLatest(createFiltersChangedObservable(), createLoadTriggerObservable(),
                BiFunction<PostsFilter, TriggerLoadPageType, Pair<PostsFilter, TriggerLoadPageType>> { filter, triggerType ->
                    Pair(filter, triggerType)
                }
        ).switchMap { (postsFilter, type) ->
            when (type){
                TriggerLoadPageType.FIRST_PAGE -> loadPosts.loadFirstPage(postsFilter.tag?.id, postsFilter.categories, postsFilter.searchQuery)
                TriggerLoadPageType.NEXT_PAGE -> loadPosts.loadNextPage(postsFilter.tag?.id, postsFilter.categories, postsFilter.searchQuery)
            }
        }

        val allIntentsObservable = stateChangeObservable
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
            is PartialPostsChanges.FirstPageLoading -> state.copy(data = Collections.emptyList(), hasMore = true, error = null)

            is PartialPostsChanges.NextPageLoading -> {
                state.copy(hasMore = true, loading = true, error = null)
            }
            is PartialPostsChanges.PostsLoadingError -> {
                state.copy(hasMore = true, loading = false, error = changes.throwable)
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

    private fun createLoadTriggerObservable(): Observable<TriggerLoadPageType>{
        val triggerLoadFirstPageObservable =  intent(PostsView::loadFirstPageIntent).map { TriggerLoadPageType.FIRST_PAGE }
        val triggerLoadNextPageObservable = intent(PostsView::loadNextPageIntent).map { TriggerLoadPageType.NEXT_PAGE }
                .filter { !isLoading }
                .filter { hasMore }
        return Observable.merge(triggerLoadFirstPageObservable, triggerLoadNextPageObservable)
    }

    private fun createFiltersChangedObservable(): Observable<PostsFilter> {

        val initTagObservable = intent(PostsView::startInitIntent)
                .flatMap { getTagInteractor.execute() }
                .map { PostsFilterChange.TagChange(it) }

        val initCategoriesObservable = intent(PostsView::startInitIntent)
                .flatMap { getCategoriesInteractor.execute() }
                .map { PostsFilterChange.CategoriesChange(it) }
        val initSearchQueryObservable = intent(PostsView::startInitIntent)
                .flatMap { getSearchQueryInteractor.execute() }
                .map { PostsFilterChange.SearchQueryChange(it) }

        val categoriesChangedObservable = activatedCategoriesManager.subscribeToUpdates()
                .map { PostsFilterChange.CategoriesChange(it) }

        return Observable.merge(categoriesChangedObservable, initCategoriesObservable, initTagObservable, initSearchQueryObservable)
                        .scan(PostsFilter(), this::postsFilterReducer)
    }

    private enum class TriggerLoadPageType {
        FIRST_PAGE, NEXT_PAGE
    }
}