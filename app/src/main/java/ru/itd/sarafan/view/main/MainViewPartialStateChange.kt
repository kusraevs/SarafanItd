package ru.itd.sarafan.view.main

import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.Category

/**
 * Created by macbook on 23.10.17.
 */
sealed class MainViewPartialStateChange {
    data class CategoriesLoaded(val data: Categories? = null, val initialRootCategory: Category? = null) : MainViewPartialStateChange()
    data class RootCategoryChanged(val rootCategory: Category, val newChildCategory: Category?) : MainViewPartialStateChange()
    data class ChildCategoryChanged(val childCategory: Category) : MainViewPartialStateChange()
    data class SearchQueryChanged(val query: String) : MainViewPartialStateChange()
    data class IsDenominatorChanged(val isDenominator: Boolean) : MainViewPartialStateChange()
}

