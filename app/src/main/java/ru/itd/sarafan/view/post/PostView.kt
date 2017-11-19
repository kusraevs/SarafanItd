package ru.itd.sarafan.view.post

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

/**
 * Created by macbook on 12.11.17.
 */
interface PostView: MvpView {
    fun startIntent(): Observable<Boolean>
    fun shareClickIntent(): Observable<Boolean>
    fun render(state: PostViewState)

}