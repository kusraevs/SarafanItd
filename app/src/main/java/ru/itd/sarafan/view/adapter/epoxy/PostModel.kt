package ru.itd.sarafan.view.adapter.epoxy

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.epoxy.*
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import ru.itd.sarafan.R
import ru.itd.sarafan.rest.model.Post
import ru.itd.sarafan.utils.DateUtils
import ru.itd.sarafan.view.adapter.TagsAdapter

/**
 * Created by macbook on 18.10.17.
 */
@EpoxyModelClass(layout = R.layout.posts_list_item)
abstract class PostModel : EpoxyModelWithHolder<PostModel.PostViewHolder>() {
    @EpoxyAttribute lateinit var post: Post
    @EpoxyAttribute var clickListener: PostClickListener? = null


    override fun createNewHolder() = PostViewHolder()

    override fun bind(holder: PostViewHolder) {
        holder.tvTitle.text = post.title?.rendered
        holder.tvText.text = Html.fromHtml(post.excerpt?.rendered)
        holder.tvDate.text = DateUtils.formatDate(post.dateGmt)

        Glide.with(holder.ivPost).load(post.embedded.medias[0].imageUrl).into(holder.ivPost)
        setUpTagsRecyclerView(holder.rvTags)

        holder.itemView.setOnClickListener { clickListener?.onPostClick(post) }
    }

    private fun setUpTagsRecyclerView(rvTags: EpoxyRecyclerView){
        val manager = FlexboxLayoutManager(rvTags.context).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.STRETCH
        }
        rvTags.layoutManager = manager
        val tagsAdapter = TagsAdapter(rvTags.context, post.embedded.getTagTerms())
        tagsAdapter.clickListener = clickListener
        rvTags.swapAdapter(tagsAdapter, false)
        rvTags.setHasFixedSize(true)
        rvTags.isNestedScrollingEnabled = false

    }


    class PostViewHolder : EpoxyHolder() {
        @BindView(R.id.tv_post_title) lateinit var tvTitle: TextView
        @BindView(R.id.tv_text) lateinit var tvText: TextView
        @BindView(R.id.tv_date) lateinit var tvDate: TextView
        @BindView(R.id.iv_post) lateinit var ivPost: ImageView
        @BindView(R.id.rv_tags) lateinit var rvTags: EpoxyRecyclerView
        lateinit var itemView: View

        override fun bindView(itemView: View) {
            ButterKnife.bind(this, itemView)
            this.itemView = itemView
        }
    }

    interface PostClickListener: TagsAdapter.TagClickListener {
        fun onPostClick(post: Post)
    }
}