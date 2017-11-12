package ru.itd.sarafan.rest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.itd.sarafan.rest.model.embedded.Embedded
import ru.itd.sarafan.rest.model.tags.Term

data class Post(val id: Int = 0,
                val date: String = "",
                @SerializedName("date_gmt")
                val dateGmt: String = "",
                val guid: Guid? = null,
                val modified: String = "",
                @SerializedName("modified_gmt")
                val modifiedGmt: String = "",
                val slug: String = "",
                val status: String = "",
                val type: String = "",
                val link: String = "",
                val title: Title? = null,
                val content: Content? = null,
                val excerpt: Excerpt? = null,
                val author: Int = 0,
                @SerializedName("featured_media")
                val featuredMedia: Int = 0,
                @SerializedName("comment_status")
                val commentStatus: String = "",
                @SerializedName("ping_status")
                val pingStatus: String = "",
                val sticky: Boolean = false,
                val template: String = "",
                val format: String = "",
                val categories: List<Int>? = null,
                val tags: List<Int>? = null,
                @SerializedName("_embedded")
                val embedded: Embedded)