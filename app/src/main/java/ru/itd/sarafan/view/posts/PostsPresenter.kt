package ru.itd.sarafan.view.posts

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.businesslogic.ActivatedCategoriesManager
import ru.itd.sarafan.rest.interactors.GetCategoriesInteractor
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
                     private val getCategoriesInteractor: GetCategoriesInteractor) : MviBasePresenter<PostsView, PostsViewState>() {

    @Inject lateinit var loadPosts: LoadPostsInteractor
    @Inject lateinit var activatedCategoriesManager: ActivatedCategoriesManager

    private var categories: Categories? = null
    private var tag: Term? = null

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

        val initObservable = Observable.merge(initCategoriesObservable, initTagObservable).take(1)

        val categoriesChangedObservable = activatedCategoriesManager.categoriesUpdatePublisher
                .doOnNext { this.categories = it }

        val loadFirstPageObservable = Observable.merge(initObservable, categoriesChangedObservable)
                .switchMap { loadPosts.loadFirstPage(tagId = tag?.id, categories = categories) }



        val loadNextPageObservable = intent(PostsView::loadNextPageIntent)
        val allLoadPagesObservable = loadNextPageObservable
                .distinctUntilChanged()
                .filter { it != 0 }
                .flatMap { loadPosts.loadNextPage(tagId = tag?.id, categories = categories) }



        val allIntentsObservable = Observable.merge(allLoadPagesObservable, loadFirstPageObservable)
                .scan(PostsViewState(), this::viewStateReducer)

        subscribeViewState(allIntentsObservable, PostsView::render)
    }


    private fun viewStateReducer(state: PostsViewState, changes: PartialPostsChanges): PostsViewState {
        return when (changes) {
            is PartialPostsChanges.NextPageLoaded -> {
                val newPosts = state.data + changes.posts
                state.copy(data = newPosts, loading = changes.hasMore)
            }
            is PartialPostsChanges.FirstPageLoaded -> {
                state.copy(data = changes.posts, loading = changes.hasMore)
            }
            /*
            is PartialPostsChanges.CategoriesUpdate -> {
                state.copy(categories = changes.categories.categories)
            }*/
            is PartialPostsChanges.FirstPageLoading -> state.copy(data = Collections.emptyList(), loading = true, error = null)

            is PartialPostsChanges.NextPageLoading -> {
                state.copy(loading = true, error = null)
            }
            is PartialPostsChanges.PostsLoadingError -> {
                state.copy(loading = false, error = changes.throwable)
            }
        }
    }

    private fun postsFilterReducer(filter: PostsFilter, change: PostsFilterChange): PostsFilter {
        return when (change){
            is PostsFilterChange.CategoriesChange -> filter.copy(categories = change.categories)
            is PostsFilterChange.TagChange -> filter.copy(tag = change.tagTerm)
        }
    }

    interface MainPresenterHolder {
        fun getMainPresenter(): MainPresenter
    }

}