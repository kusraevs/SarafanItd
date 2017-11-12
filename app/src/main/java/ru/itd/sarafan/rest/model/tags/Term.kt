package ru.itd.sarafan.rest.model.tags

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by macbook on 18.10.17.
 */
class Term(
        //"post_tag" for tag
        @SerializedName("taxonomy")
        val taxonomy: String,

        @SerializedName("name")
        val name: String,

        @SerializedName("id")
        val id: Int,


        @SerializedName("_links")
        val links: TermLinks
): Serializable