package ru.itd.sarafan.rest.model.embedded

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by macbook on 18.10.17.
 */
data class Image(
        @Expose
        @SerializedName("source_url")
        val imageUrl: String
)