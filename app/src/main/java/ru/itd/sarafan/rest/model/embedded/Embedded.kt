package ru.itd.sarafan.rest.model.embedded

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.itd.sarafan.rest.model.tags.Term
import java.util.*

/**
 * Created by macbook on 18.10.17.
 */
data class Embedded(
        @Expose
        @SerializedName("wp:featuredmedia")
        val medias: List<Media>
        ,
        @SerializedName("wp:term")
        val terms: List<List<Term>>
) {

    fun getTagTerms(): List<Term> = if (terms.size > 1) terms[1] else Collections.emptyList()


}