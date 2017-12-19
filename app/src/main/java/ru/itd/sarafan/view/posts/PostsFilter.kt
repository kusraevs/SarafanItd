package ru.itd.sarafan.view.posts

import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.tags.Term

/**
 * Created by macbook on 26.10.17.
 */
data class PostsFilter(var categories: Categories? = null, var tag: Term? = null, var searchQuery: String? = null)