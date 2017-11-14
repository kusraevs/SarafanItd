package ru.itd.sarafan.rest.interactors

import io.reactivex.Observable
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.businesslogic.ActivatedCategoriesManager
import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.Category
import ru.itd.sarafan.view.main.MainViewPartialStateChange
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by macbook on 23.10.17.
 */
class ChangeChildCategoryInteractor {

    @Inject lateinit var activatedCategoriesManager: ActivatedCategoriesManager

    init {
        SarafanApplication.getComponent().inject(this)
    }

    fun execute(category: Category): Observable<MainViewPartialStateChange> {
        return Observable.just(category)
                .doOnNext { activatedCategoriesManager.setActivatedCategories(categories = Categories(listOf(category))) }
                .map { MainViewPartialStateChange.ChildCategoryChanged(category) }
    }
}