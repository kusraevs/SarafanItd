package ru.itd.sarafan.view.posts

import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.tags.Term

/**
 * Created by macbook on 26.10.17.
 */
data class PostsFilter(val categories: Categories? = null, val tag: Term? = null, val searchQuery: String? = null)