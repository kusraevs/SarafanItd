package ru.itd.sarafan.rest.model.tags

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by macbook on 19.10.17.
 */
class Link(
        @SerializedName("href")
        val url: String
): Serializable