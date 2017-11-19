package ru.itd.sarafan.di

import dagger.Module
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Singleton
import dagger.Provides
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Cicerone



/**
 * Created by macbook on 19.11.17.
 */
@Module
class NavigationModule {
    private val cicerone: Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    internal fun provideRouter(): Router {
        return cicerone.router
    }

    @Provides
    @Singleton
    internal fun provideNavigatorHolder(): NavigatorHolder {
        return cicerone.navigatorHolder
    }
}