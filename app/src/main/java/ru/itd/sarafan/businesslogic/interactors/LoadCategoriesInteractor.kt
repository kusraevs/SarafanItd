package ru.itd.sarafan.businesslogic.interactors

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.businesslogic.CategoriesTreeBuilder
import ru.itd.sarafan.rest.CategoriesLoader
import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.view.splash.SplashState
import javax.inject.Inject

/**
 * Created by macbook on 23.10.17.
 */
class LoadCategoriesInteractor {
    @Inject lateinit var categoriesLoader: CategoriesLoader


    init {
        SarafanApplication.getComponent().inject(this)
    }


    fun loadCategories(): Observable<SplashState> {
        return categoriesLoader.getCategories()
                .map(CategoriesTreeBuilder())
                .sorted()
                .map { SplashState(data = Categories(it)) }
               // .startWith{ SplashState(hasMore = true) }
                .onErrorReturn {
                    it.printStackTrace()
                    SplashState(error = it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


}