package ru.itd.sarafan.di

import dagger.Component
import ru.itd.sarafan.businesslogic.interactors.*
import ru.itd.sarafan.rest.CategoriesLoader
import ru.itd.sarafan.rest.PostsLoader
import ru.itd.sarafan.view.main.MainActivity
import ru.itd.sarafan.view.post.PostActivity
import ru.itd.sarafan.view.post.PostPresenter
import ru.itd.sarafan.view.posts.PostsFragment
import ru.itd.sarafan.view.posts.PostsPresenter
import ru.itd.sarafan.view.splash.SplashPresenter
import javax.inject.Singleton

/**
 * Created by macbook on 17.10.17.
 */
@Singleton
@Component(modules = arrayOf(RestApiModule::class, AppModule::class,
        PostsModule::class, CategoriesModule::class,
        MemoryManagerModule::class, NavigationModule::class))
interface AppComponent {
    fun inject(postsFragment: PostsFragment)
    fun inject(postActivity: PostActivity)
    fun inject(postsPresenter: PostsPresenter)
    fun inject(postPresenter: PostPresenter)
    fun inject(postsLoader: PostsLoader)


    fun inject(categoriesLoader: CategoriesLoader)
    fun inject(splashPresenter: SplashPresenter)
    fun inject(loadPostsInteractor: LoadPostsInteractor)
    fun inject(loadCategoriesInteractor: LoadCategoriesInteractor)

    //fun inject(activatedCategoriesManager: ActivatedCategoriesManager)
    fun inject(changeChildCategoryInteractor: ChangeChildCategoryInteractor)
    fun inject(changeRootCategoryInteractor: ChangeRootCategoryInteractor)
    fun inject(subscribeToCategoriesInteractor: SubscribeToCategoriesInteractor)

    fun inject(mainActivity: MainActivity)
}