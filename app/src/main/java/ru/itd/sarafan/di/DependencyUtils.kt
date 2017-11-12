package ru.itd.sarafan.di

import io.reactivex.Observable
import ru.itd.sarafan.rest.model.Category
import ru.itd.sarafan.view.main.MainPresenter

/**
 * Created by macbook on 24.10.17.
 */
class DependencyUtils {


    fun createCategoriesUpdateObservable(presenter: MainPresenter?): Observable<List<Category>> {
        if (presenter == null)
            return Observable.empty()
        return presenter.viewStateObservable
                .filter { it.childCategory != null }
                .map { it.childCategory!! }
                .map { it.childs ?: listOf(it) }
    }
}