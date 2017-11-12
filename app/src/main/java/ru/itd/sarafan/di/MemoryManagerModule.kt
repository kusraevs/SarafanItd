package ru.itd.sarafan.di

import dagger.Module
import dagger.Provides
import ru.itd.sarafan.businesslogic.ActivatedCategoriesManager
import javax.inject.Singleton

/**
 * Created by macbook on 26.10.17.
 */
@Module
@Singleton
class MemoryManagerModule {
    @Provides
    @Singleton
    fun provideActivatedCategoriesManager(): ActivatedCategoriesManager {
        return ActivatedCategoriesManager()
    }
}