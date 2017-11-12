package ru.itd.sarafan.businesslogic

import io.reactivex.subjects.BehaviorSubject
import ru.itd.sarafan.rest.model.Categories

/**
 * Created by macbook on 26.10.17.
 */
class ActivatedCategoriesManager {
    val categoriesUpdatePublisher: BehaviorSubject<Categories> = BehaviorSubject.create<Categories>()

    fun setActivatedCategories(categories: Categories){
        categoriesUpdatePublisher.onNext(categories)
    }
}