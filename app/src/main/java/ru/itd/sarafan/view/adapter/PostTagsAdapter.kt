package ru.itd.sarafan.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.itd.sarafan.R
import ru.itd.sarafan.rest.model.tags.Term

/**
 * Created by macbook on 14.11.17.
 */
class PostTagsAdapter(context: Context, data: List<Term>): TagsAdapter(context, data) {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            TagViewHolder(LayoutInflater.from(context).inflate(R.layout.tags_post_list_item, null, false))

}