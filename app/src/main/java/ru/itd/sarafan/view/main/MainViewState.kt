package ru.itd.sarafan.view.main

import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.Category

/**
 * Created by macbook on 23.10.17.
 */
data class MainViewState(val data: Categories? = null,
                         val rootCategory: Category? = null,
                         val childCategory : Category? = null,
                         val navigateToSearchWithQuery: String = "") {
}