package ru.itd.sarafan.view.posts

import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.tags.Term

/**
 * Created by macbook on 26.10.17.
 */
sealed class PostsFilterChange(){
    data class CategoriesChange(val categories: Categories): PostsFilterChange()
    data class TagChange(val tagTerm: Term): PostsFilterChange()
}