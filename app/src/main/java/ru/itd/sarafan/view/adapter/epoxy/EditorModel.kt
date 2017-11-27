package ru.itd.sarafan.view.adapter.epoxy

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.itd.sarafan.R
import ru.itd.sarafan.rest.model.editors.Editor

/**
 * Created by macbook on 19.10.17.
 */
@EpoxyModelClass(layout = R.layout.editor_list_item)
abstract class EditorModel : EpoxyModelWithHolder<EditorModel.EditorViewHolder>() {
    @EpoxyAttribute var clickListener: EditorClickListener? = null
    @EpoxyAttribute var editor: Editor? = null

    override fun createNewHolder() = EditorViewHolder()

    override fun bind(holder: EditorViewHolder) {
        holder.ivEditorPhoto.setImageDrawable(editor?.image)
        holder.itemView.setOnClickListener { clickListener?.onEditorClick() }
        holder.tvName.text = editor?.name
        holder.tvTitle.text = editor?.title
    }


    class EditorViewHolder : BaseEpoxyHolder() {
        @BindView(R.id.iv_photo) lateinit var ivEditorPhoto: ImageView
        @BindView(R.id.tv_name) lateinit var tvName: TextView
        @BindView(R.id.tv_title) lateinit var tvTitle: TextView

    }

    interface EditorClickListener {
        fun onEditorClick()
    }

}