package ru.itd.sarafan.view.posts

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed3EpoxyController
import ru.itd.sarafan.rest.model.Post
import ru.itd.sarafan.view.adapter.epoxy.LoadingPostsErrorModel_
import ru.itd.sarafan.view.adapter.epoxy.LoadingPostsModel_
import ru.itd.sarafan.view.adapter.epoxy.PostModel
import ru.itd.sarafan.view.adapter.epoxy.PostModel_


/**
 * Created by macbook on 16.10.17.
 */
class PostsController(val clickListener: ItemClickListener? = null): Typed3EpoxyController<List<Post>, Boolean, Boolean>() {
    @AutoModel lateinit var loader: LoadingPostsModel_
    @AutoModel lateinit var error: LoadingPostsErrorModel_


    override fun buildModels(data: List<Post>, hasMore: Boolean, isError: Boolean) {
        data.forEach { post ->
            PostModel_().post(post)
                    .id(post.id)
                    .clickListener(clickListener)
                    .addTo(this)
        }
        loader.addIf(hasMore, this);
        error.addIf(isError, this)
    }

    interface ItemClickListener: PostModel.PostClickListener
}