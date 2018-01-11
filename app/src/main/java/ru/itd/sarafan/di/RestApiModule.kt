package ru.itd.sarafan.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.itd.sarafan.rest.RestApi
import ru.itd.sarafan.rest.RestApiFactory

/**
 * Created by macbook on 17.10.17.
 */
@Module
class RestApiModule(private val context: Context) {

    @Provides
    fun provideRestApi(): RestApi {
        return RestApiFactory.getRestApi(context)
    }
}