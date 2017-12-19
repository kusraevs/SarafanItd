package ru.itd.sarafan.view.main

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import ru.itd.sarafan.rest.model.Category

/**
 * Created by macbook on 23.10.17.
 */
interface MainView: MvpView {

    fun startLoadingIntent(): Observable<Boolean>
    fun getCurrentWeekTypeIntent(): Observable<Boolean>

    fun rootCategorySelected(): Observable<Category>
    fun childCategorySelected(): Observable<Category>
    fun searchTextSubmitted(): Observable<String>

    fun render(state: MainViewState)
}