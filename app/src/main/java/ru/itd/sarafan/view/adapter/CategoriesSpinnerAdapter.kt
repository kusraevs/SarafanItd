package ru.itd.sarafan.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import ru.itd.sarafan.R
import ru.itd.sarafan.rest.model.Category


/**
 * Created by macbook on 22.10.17.
 */
class CategoriesSpinnerAdapter(context: Context, items: List<Category>):
        ArrayAdapter<Category>(context, R.layout.category_child_list_item, R.id.spinner_text, items) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View
        if (convertView == null) {
            view = inflater.inflate(R.layout.category_child_list_dropdown_item, parent, false)
            view.tag = ChildCategoryViewHolder(view)
        } else
            view = convertView

        val viewHolder = view.tag as ChildCategoryViewHolder
        viewHolder.tvTitle.text = getItem(position).name
        return view
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return super.getView(position, convertView, parent)
    }

    class ChildCategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.spinner_text) lateinit var tvTitle: TextView
        init {
            ButterKnife.bind(this, itemView)
        }
        companion object {
            fun create(inflater: LayoutInflater): TagsAdapter.TagViewHolder {
                return TagsAdapter.TagViewHolder(inflater.inflate(R.layout.category_child_list_item, null, false))
            }
        }

    }
}

