package ru.itd.sarafan.view.editorship

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

/**
 * Created by macbook on 28.11.2017.
 */
interface EditorshipView: MvpView {
    fun startIntent(): Observable<Boolean>
    fun render(state: EditorshipState)
}