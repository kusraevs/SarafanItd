package ru.itd.sarafan

import android.app.Application
import dagger.android.DaggerActivity
import ru.itd.sarafan.di.*


/**
 * Created by macbook on 17.10.17.
 */

class SarafanApplication: Application(){


    override fun onCreate(){
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .postsModule(PostsModule(this))
                .restApiModule(RestApiModule(this))
                .build()
    }

    companion object {
        lateinit var appComponent: AppComponent

        fun getComponent(): AppComponent {
            return appComponent
        }
    }

}