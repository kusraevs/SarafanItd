package ru.itd.sarafan.di

import dagger.Module
import dagger.Provides
import ru.itd.sarafan.rest.CategoriesLoader
import ru.itd.sarafan.businesslogic.interactors.LoadCategoriesInteractor

/**
 * Created by macbook on 20.10.17.
 */
@Module
class CategoriesModule {
    @Provides
    fun provideCategoriesLoader(): CategoriesLoader {
        return CategoriesLoader()
    }
    @Provides
    fun provideLoadCategoriesInteractor(): LoadCategoriesInteractor {
        return LoadCategoriesInteractor()
    }
}