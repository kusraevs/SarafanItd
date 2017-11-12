package ru.itd.sarafan.rest

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.itd.sarafan.rest.model.Category
import ru.itd.sarafan.rest.model.Post
/**
 * Created by macbook on 16.10.17.
 */
interface RestApi {


    @GET("posts/")
    fun getPosts(@Query("tags") tags: List<Int>? = null, @Query("categories") categories: List<Int>? = null,
            @Query("page") page: Int, @Query("_embed") embedded: Boolean = true): Observable<List<Post>>

    @GET("categories/")
    fun getCategories(@Query("per_page") limit: Int? = 100): Observable<List<Category>>
}