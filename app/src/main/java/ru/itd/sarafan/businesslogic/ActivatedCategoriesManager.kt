package ru.itd.sarafan.businesslogic

import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import ru.itd.sarafan.rest.model.Categories

/**
 * Created by macbook on 26.10.17.
 */
class ActivatedCategoriesManager {
    val categoriesUpdatePublisher: PublishSubject<Categories> = PublishSubject.create<Categories>()

    fun setActivatedCategories(categories: Categories){
        categoriesUpdatePublisher.onNext(categories)
    }
}