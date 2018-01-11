package ru.itd.sarafan.rest

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.itd.sarafan.rest.factory.LoggingInterceptor
import ru.itd.sarafan.rest.factory.MockInterceptor
import ru.itd.sarafan.rest.factory.UnzippingInterceptor
import java.util.concurrent.TimeUnit


/**
 * Created by macbook on 16.10.17.
 */
class RestApiFactory {

    companion object {
        private val BASE_URL = "http://sarafanitd.ru/wp-json/wp/v2"

        private var restApi: RestApi? = null


        @Synchronized
        fun getRestApi(context: Context): RestApi {
            if (restApi == null) {
                val httpClient = OkHttpClient.Builder()
                        .addInterceptor(MockInterceptor(context))
                        .addNetworkInterceptor(LoggingInterceptor())
                        .addNetworkInterceptor(UnzippingInterceptor())
                        .retryOnConnectionFailure(true)
                        .connectionPool(ConnectionPool(5, 5, TimeUnit.SECONDS))
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build()
                restApi = Retrofit.Builder()
                        .baseUrl(BASE_URL + "/")
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient)
                        .build().create(RestApi::class.java)
            }
            return restApi!!
        }


    }
}