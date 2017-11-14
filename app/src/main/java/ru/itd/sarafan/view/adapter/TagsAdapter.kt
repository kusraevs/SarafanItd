package ru.itd.sarafan.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import ru.itd.sarafan.R
import ru.itd.sarafan.rest.model.tags.Term
import android.support.annotation.CallSuper
import com.airbnb.epoxy.EpoxyHolder




/**
 * Created by macbook on 19.10.17.
 */
open class TagsAdapter(val context: Context, var data: List<Term>): RecyclerView.Adapter<TagsAdapter.TagViewHolder>() {
    var clickListener: TagClickListener? = null

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.tvTagTitle.text = data[position].name.toLowerCase()
        holder.itemView.setOnClickListener { clickListener?.onTagClicked(data[position]) }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            TagViewHolder(LayoutInflater.from(context).inflate(R.layout.tags_list_item, null, false))
    override fun getItemCount() = data.size

    class TagViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.tv_tag_title) lateinit var tvTagTitle: TextView
        init {
            ButterKnife.bind(this, itemView)
        }
    }

    interface TagClickListener {
        fun onTagClicked(tagTerm: Term)
    }


}