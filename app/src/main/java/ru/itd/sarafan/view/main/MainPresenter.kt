package ru.itd.sarafan.view.main

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import ru.itd.sarafan.rest.interactors.ChangeChildCategoryInteractor
import ru.itd.sarafan.rest.interactors.ChangeRootCategoryInteractor
import ru.itd.sarafan.rest.interactors.GetCategoriesInteractor

/**
 * Created by macbook on 23.10.17.
 */
class MainPresenter(private val getCategoriesInteractor: GetCategoriesInteractor,
                    private val changeRootCategoryInteractor: ChangeRootCategoryInteractor,
                    private val changeChildCategoryInteractor: ChangeChildCategoryInteractor): MviBasePresenter<MainView, MainViewState>() {

    override fun bindIntents() {

        val getCategoriesObservable = intent(MainView::startLoadingObservable)
                .flatMap { getCategoriesInteractor.execute() }
                .map { MainViewPartialStateChange.CategoriesLoaded(data = it) as MainViewPartialStateChange }

        val changeRootCategoryObservable = intent(MainView::rootCategorySelected).flatMap { changeRootCategoryInteractor.execute(it) }
        val changeChildCategoryObservable = intent(MainView::childCategorySelected).flatMap { changeChildCategoryInteractor.execute(it) }

        val allIntentsObservable = Observable.merge(getCategoriesObservable, changeRootCategoryObservable, changeChildCategoryObservable)
                .scan(MainViewState(), this::viewStateReducer)
                .doOnError{ it.printStackTrace() }
                //TODO(Move interactors to background thread)
                //.subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
        subscribeViewState(allIntentsObservable, MainView::render)
    }

    private fun viewStateReducer(state: MainViewState, changes: MainViewPartialStateChange): MainViewState {
        return when (changes) {
            is MainViewPartialStateChange.CategoriesLoaded -> {
                state.copy(data = changes.data)
            }
            is MainViewPartialStateChange.RootCategoryChanged -> {
                state.copy(rootCategory = changes.rootCategory, childCategory = changes.newChildCategory)
            }
            is MainViewPartialStateChange.ChildCategoryChanged -> {
                state.copy(childCategory = changes.childCategory)
            }
        }
    }

    public override fun getViewStateObservable(): Observable<MainViewState> {
        return super.getViewStateObservable()
    }

}