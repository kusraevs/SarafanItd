package ru.itd.sarafan.rest.model

import java.io.Serializable
import java.util.*

/**
 * Created by macbook on 22.10.17.
 */
class Categories(val categories: List<Category> = Collections.emptyList()): Serializable {
    fun getIds() = categories.map { it.id }
}