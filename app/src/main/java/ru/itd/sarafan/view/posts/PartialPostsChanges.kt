package ru.itd.sarafan.view.posts

import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.Post

/**
 * Created by macbook on 19.10.17.
 */
sealed class PartialPostsChanges {
    data class NextPageLoaded(val posts: List<Post>, val hasMore: Boolean = true) : PartialPostsChanges()
    data class FirstPageLoaded(val posts: List<Post>, val hasMore: Boolean = true) : PartialPostsChanges()

    class NextPageLoading : PartialPostsChanges()
    class FirstPageLoading : PartialPostsChanges()

    class PostsLoadingError(val throwable: Throwable): PartialPostsChanges()

}

