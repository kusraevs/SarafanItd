package ru.itd.sarafan.rest

import io.reactivex.Observable
import retrofit2.http.Query
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.rest.model.Post
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by macbook on 16.10.17.
 */
class PostsLoader() {
    @Inject lateinit var restApi: RestApi


    init {
        SarafanApplication.getComponent().inject(this)
    }

    fun getPosts(tags: List<Int>? = null, categories: List<Int>? = null, searchQuery: String?, page: Int): Observable<List<Post>> {
        return restApi.getPosts(tags, categories, searchQuery, page)//.delay(1, TimeUnit.SECONDS)
    }


}