package ru.itd.sarafan.di

import android.app.Application
import dagger.Module
import dagger.Provides

/**
 * Created by macbook on 17.10.17.
 */
@Module
class AppModule(val application: Application) {
    @Provides
    fun provideContext() = application
}