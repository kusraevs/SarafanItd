package ru.itd.sarafan.rest.model

import java.io.Serializable

data class Content(val rendered: String = "",
                   val protected: Boolean = false) : Serializable