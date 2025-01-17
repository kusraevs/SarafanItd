package ru.itd.sarafan.rest.model.embedded

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by macbook on 18.10.17.
 */
data class Media(
        @Expose
        @SerializedName("media_details")
        val details: MediaDetails,

        @Expose
        @SerializedName("source_url")
        val imageUrl: String
): Serializable