package ru.itd.sarafan.rest.interactors

import io.reactivex.Observable
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.businesslogic.ActivatedCategoriesManager
import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.Category
import ru.itd.sarafan.view.main.MainViewPartialStateChange
import javax.inject.Inject

/**
 * Created by macbook on 23.10.17.
 */
class ChangeRootCategoryInteractor {
    @Inject lateinit var activatedCategoriesManager: ActivatedCategoriesManager

    init {
        SarafanApplication.getComponent().inject(this)
    }

    fun execute(category: Category): Observable<MainViewPartialStateChange>{
        return Observable.just(category)
                .map { getChildsListWithParentAtTheStart(category) }
                .doOnNext { activatedCategoriesManager.setActivatedCategories(Categories(it)) }
                .map { categoryList -> MainViewPartialStateChange.RootCategoryChanged(category.copy(childs = categoryList), categoryList[0])
        }
    }

    private fun getChildsListWithParentAtTheStart(category: Category): List<Category>{
        return if (category.childs == null || category.childs.isEmpty())
            listOf(category)
        else
            listOf(category) + category.childs

    }
}