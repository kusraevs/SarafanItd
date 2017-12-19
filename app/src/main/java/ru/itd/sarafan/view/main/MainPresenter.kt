package ru.itd.sarafan.view.main

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import ru.itd.sarafan.businesslogic.interactors.ChangeChildCategoryInteractor
import ru.itd.sarafan.businesslogic.interactors.ChangeRootCategoryInteractor
import ru.itd.sarafan.businesslogic.interactors.GetCategoriesInteractor
import ru.itd.sarafan.businesslogic.interactors.GetWeekTypeInteractor

/**
 * Created by macbook on 23.10.17.
 */
class MainPresenter(private val getCategoriesInteractor: GetCategoriesInteractor,
                    private val changeRootCategoryInteractor: ChangeRootCategoryInteractor,
                    private val changeChildCategoryInteractor: ChangeChildCategoryInteractor,
                    private val getWeekTypeInteractor: GetWeekTypeInteractor): MviBasePresenter<MainView, MainViewState>() {

    override fun bindIntents() {

        val getCategoriesObservable = intent(MainView::startLoadingIntent)
                .flatMap { getCategoriesInteractor.execute() }
                .map { MainViewPartialStateChange.CategoriesLoaded(data = it) as MainViewPartialStateChange }

        val isDenominatorChangedObservable = intent(MainView::getCurrentWeekTypeIntent).flatMap { getWeekTypeInteractor.execute() }
                .map { MainViewPartialStateChange.IsDenominatorChanged(it) }

        val changeRootCategoryObservable = intent(MainView::rootCategorySelected)
        val changeChildCategoryObservable = intent(MainView::childCategorySelected)
        val changeCategoryObservable = Observable.merge(changeRootCategoryObservable, changeChildCategoryObservable)
                .distinctUntilChanged()
                .flatMap {
                    if (it.parent == 0)
                        changeRootCategoryInteractor.execute(it)
                    else
                        changeChildCategoryInteractor.execute(it)
                }

        val searchTextSubmittedObservable = intent(MainView::searchTextSubmitted).map { MainViewPartialStateChange.SearchQueryChanged(it) }
        val allIntentsObservable = Observable.merge(getCategoriesObservable, changeCategoryObservable,
                searchTextSubmittedObservable, isDenominatorChangedObservable)
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
                state.copy(data = changes.data, rootCategory = changes.initialRootCategory)
            }
            is MainViewPartialStateChange.RootCategoryChanged -> {
                state.copy(rootCategory = changes.rootCategory, childCategory = changes.newChildCategory)
            }
            is MainViewPartialStateChange.ChildCategoryChanged -> {
                state.copy(childCategory = changes.childCategory)
            }
            is MainViewPartialStateChange.SearchQueryChanged -> {
                state.copy(navigateToSearchWithQuery = changes.query)
            }
            is MainViewPartialStateChange.IsDenominatorChanged -> {
                state.copy(isDenominator = changes.isDenominator)
            }
        }
    }

    public override fun getViewStateObservable(): Observable<MainViewState> {
        return super.getViewStateObservable()
    }

}