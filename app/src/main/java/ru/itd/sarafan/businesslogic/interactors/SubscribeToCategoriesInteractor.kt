package ru.itd.sarafan.businesslogic.interactors

import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.businesslogic.ActivatedCategoriesManager
import javax.inject.Inject

/**
 * Created by macbook on 23.11.17.
 */
class SubscribeToCategoriesInteractor {
    @Inject lateinit var activatedCategoriesManager: ActivatedCategoriesManager

    init {
        SarafanApplication.getComponent().inject(this)
    }

    fun execute() = activatedCategoriesManager.subscribeToUpdates()
}