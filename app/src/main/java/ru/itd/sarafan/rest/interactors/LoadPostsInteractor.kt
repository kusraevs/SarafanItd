package ru.itd.sarafan.rest.interactors

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.rest.PostsLoader
import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.view.posts.PartialPostsChanges
import java.util.*
import javax.inject.Inject

/**
 * Created by macbook on 18.10.17.
 */
class LoadPostsInteractor {
    @Inject lateinit var postsLoader: PostsLoader

    private var currentPage = 1
    private var noNextPage = false

    init {
        SarafanApplication.getComponent().inject(this)
    }

    fun loadFirstPage(tagId :Int?, categories: Categories?, searchQuery: String?): Observable<PartialPostsChanges>{
        noNextPage = false
        currentPage = 1
        return loadNextPage(tagId, categories, searchQuery)
    }


    fun loadNextPage(tagId :Int?, categories: Categories?, searchQuery: String? = null): Observable<PartialPostsChanges>{
        val tags = if (tagId != null) arrayListOf(tagId) else null
        return loadNextPage(tags = tags, categories = categories?.getIds(), searchQuery = searchQuery, isFirstPage = currentPage == 1)
    }




    private fun loadNextPage(tags: List<Int>? = null, categories: List<Int>? = null, searchQuery: String? = null, isFirstPage: Boolean): Observable<PartialPostsChanges>{
        if (noNextPage)
            return Observable.just(PartialPostsChanges.NextPageLoaded(Collections.emptyList()))
        return postsLoader.getPosts(tags, categories,  searchQuery, currentPage)
                .doOnNext { currentPage++ }
                .doOnNext { if (it.isEmpty()) noNextPage = true }
                .map {
                    if (isFirstPage)
                        PartialPostsChanges.FirstPageLoaded(it)
                    else
                        PartialPostsChanges.NextPageLoaded(it)

                }.startWith(
                        if (isFirstPage)
                            PartialPostsChanges.FirstPageLoading()
                        else
                            PartialPostsChanges.NextPageLoading()
                )
                .onErrorReturn{
                    if (it is HttpException)
                        PartialPostsChanges.NextPageLoaded(Collections.emptyList(), false)
                    else
                        PartialPostsChanges.PostsLoadingError(it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }



}