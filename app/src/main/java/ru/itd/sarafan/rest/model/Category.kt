package ru.itd.sarafan.rest.model

import java.io.Serializable
import java.util.*

data class Category(val id: Int = 0,
                    val count: Int = 0,
                    val description: String = "",
                    val link: String = "",
                    val name: String = "",
                    val slug: String = "",
                    val taxonomy: String = "",
                    val parent: Int = 0,
                    val childs: List<Category> = Collections.emptyList()): Serializable {

    override fun toString(): String {
        return name
    }
}