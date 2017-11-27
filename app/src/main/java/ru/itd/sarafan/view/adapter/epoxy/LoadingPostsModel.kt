package ru.itd.sarafan.view.adapter.epoxy

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.itd.sarafan.R


/**
 * Created by macbook on 19.10.17.
 */
@EpoxyModelClass(layout = R.layout.posts_list_item_loading)
abstract class LoadingPostsModel: EpoxyModelWithHolder<LoadingPostsModel.LoadingPostsHolder>() {
    @EpoxyAttribute var don: String = ""

    override fun createNewHolder() = LoadingPostsHolder()

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int) = totalSpanCount

    class LoadingPostsHolder : BaseEpoxyHolder()
}