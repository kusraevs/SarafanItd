package ru.itd.sarafan.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.itd.sarafan.rest.PostsLoader
import ru.itd.sarafan.rest.interactors.LoadPostsInteractor
import ru.itd.sarafan.rest.model.tags.Term
import ru.itd.sarafan.view.posts.PostsPresenter

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