package ru.itd.sarafan.rest.model

import java.io.Serializable

data class Excerpt(val rendered: String = "",
                   val protected: Boolean = false) : Serializable