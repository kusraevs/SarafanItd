package ru.itd.sarafan.rest

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.rest.model.Category
import javax.inject.Inject

/**
 * Created by macbook on 16.10.17.
 */
class CategoriesLoader() {
    @Inject lateinit var restApi: RestApi

    private val categoriesUpdatePublisher = BehaviorSubject.create<List<Category>>()

    init {
        SarafanApplication.getComponent().inject(this)
    }

    fun getCategories(): Observable<List<Category>> {
        return restApi.getCategories().doOnNext { categoriesUpdatePublisher.onNext(it) }
    }

    fun subscribeToCategoriesUpdate(): Observable<List<Category>> = categoriesUpdatePublisher


}