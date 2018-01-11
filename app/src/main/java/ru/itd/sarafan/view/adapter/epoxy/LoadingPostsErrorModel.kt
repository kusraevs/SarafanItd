package ru.itd.sarafan.view.adapter.epoxy

import android.view.View
import butterknife.BindView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.itd.sarafan.R

/**
 * Created by macbook on 19.10.17.
 */
@EpoxyModelClass(layout = R.layout.posts_list_item_loading_error)
abstract class LoadingPostsErrorModel : EpoxyModelWithHolder<LoadingPostsErrorModel.LoadingPostsErrorHolder>() {
    @EpoxyAttribute var clickListener: LoadingPostErrorClickListener? = null

    override fun createNewHolder() = LoadingPostsErrorHolder()

    override fun bind(holder: LoadingPostsErrorHolder) {
        holder.loadAgainView.setOnClickListener { clickListener?.onLoadingPostErrorClick() }
    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int) = totalSpanCount

    class LoadingPostsErrorHolder : BaseEpoxyHolder() {
        @BindView(R.id.b_load_again) lateinit var loadAgainView: View

    }

    interface LoadingPostErrorClickListener {
        fun onLoadingPostErrorClick()
    }

}