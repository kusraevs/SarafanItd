package ru.itd.sarafan.view.posts

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

/**
 * Created by macbook on 16.10.17.
 */
interface PostsView: MvpView {
    fun startInitIntent(): Observable<Boolean>
    fun loadFirstPageIntent(): Observable<Boolean>
    fun loadNextPageIntent(): Observable<Boolean>

    fun render(state: PostsViewState)
}