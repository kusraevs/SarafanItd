package ru.itd.sarafan.businesslogic.interactors

import ru.itd.sarafan.rest.model.Categories

/**
 * Created by macbook on 23.10.17.
 */
class GetCategoriesInteractor(val categories: Categories?): GetInteractor<Categories>(categories)