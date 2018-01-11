package ru.itd.sarafan.view.editorship

import com.airbnb.epoxy.TypedEpoxyController
import ru.itd.sarafan.rest.model.editors.Editor
import ru.itd.sarafan.view.adapter.epoxy.EditorModel
import ru.itd.sarafan.view.adapter.epoxy.EditorModel_

/**
 * Created by macbook on 16.10.17.
 */
class EditorsController(val clickListener: ItemClickListener? = null): TypedEpoxyController<List<Editor>>() {

    override fun buildModels(data: List<Editor>) {
        data.forEach { editor ->
            EditorModel_().editor(editor)
                    .id(editor.name.hashCode())
                    .addTo(this)
        }
    }

    interface ItemClickListener: EditorModel.EditorClickListener
}