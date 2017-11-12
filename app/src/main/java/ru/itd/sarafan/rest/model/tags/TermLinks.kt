package ru.itd.sarafan.rest.model.tags

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by macbook on 19.10.17.
 */
class TermLinks(
    @SerializedName("wp:post_type")
    val link: List<Link>
): Serializable
{

    fun getTagLink() = link[0].url
}