package ru.itd.sarafan.view.posts

import ru.itd.sarafan.rest.model.Post
import java.util.*

/**
 * Created by macbook on 16.10.17.
 */
data class PostsViewState(val data: List<Post> = Collections.emptyList(),
                          val loading: Boolean = false,
                          val error: Throwable? = null)