package ru.itd.sarafan.view.drawer

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import ru.itd.sarafan.R
import ru.itd.sarafan.rest.model.Category
import ru.itd.sarafan.rest.model.tags.Term
import ru.itd.sarafan.view.adapter.TagsAdapter

/**
 * Created by macbook on 21.10.17.
 */
class RootCategoriesAdapter(val context: Context, var data: List<Category>, var selectedCategory: Category? = null): RecyclerView.Adapter<RootCategoriesAdapter.RootCategoryViewHolder>() {
    var clickListener: RootCategoryClickListener? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onBindViewHolder(holder: RootCategoryViewHolder, position: Int) {
        holder.tvCategoryTitle.text = data[position].name.toUpperCase()
        holder.itemView.setOnClickListener { clickListener?.onRootCategoryClicked(data[position]) }

        if (selectedCategory != null && data[position].id == selectedCategory!!.id){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.navigation_drawer_selected_color))
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RootCategoryViewHolder {
        return RootCategoryViewHolder(inflater.inflate(R.layout.category_list_item, parent, false))
    }

    override fun getItemCount() = data.size

    class RootCategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.tv_category_title) lateinit var tvCategoryTitle: TextView
        init {
            ButterKnife.bind(this, itemView)
        }
    }

    interface RootCategoryClickListener {
        fun onRootCategoryClicked(category: Category)
    }


}