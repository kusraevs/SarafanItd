package ru.itd.sarafan.view.editorship

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import ru.itd.sarafan.rest.EditorsLoader

/**
 * Created by macbook on 28.11.2017.
 */
class EditorshipPresenter(private val editorsLoader: EditorsLoader) : MviBasePresenter<EditorshipView, EditorshipState>() {
    override fun bindIntents() {
        val allIntents = intent(EditorshipView::startIntent).flatMap { editorsLoader.loadEditors() }.map { EditorshipState(it) }
        subscribeViewState(allIntents, EditorshipView::render)
    }
}