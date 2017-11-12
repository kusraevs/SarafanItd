package ru.itd.sarafan.view.splash

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

/**
 * Created by macbook on 23.10.17.
 */
interface SplashView: MvpView {
    fun loadCategoriesIntent(): Observable<Boolean>

    fun render(state: SplashState)
}