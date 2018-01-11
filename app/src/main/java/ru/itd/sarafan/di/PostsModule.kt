package ru.itd.sarafan.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.itd.sarafan.businesslogic.interactors.LoadPostsInteractor
import ru.itd.sarafan.rest.PostsLoader

/**
 * Created by macbook on 17.10.17.
 */
@Module
class PostsModule(val context: Context) {

    @Provides
    fun providePostsLoader(): PostsLoader {
        return PostsLoader()
    }
    @Provides
    fun provideLoadPostsInteractor(): LoadPostsInteractor {
        return LoadPostsInteractor()
    }

}