package ru.itd.sarafan.view.splash

import android.content.Intent
import android.os.Bundle
import com.hannesdorfmann.mosby3.mvi.MviActivity
import io.reactivex.Observable
import ru.itd.sarafan.view.main.MainActivity
import ru.itd.sarafan.R
import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.utils.RouterUtils

class SplashActivity : MviActivity<SplashView, SplashPresenter>(), SplashView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun createPresenter(): SplashPresenter {
        return SplashPresenter()
    }

    override fun loadCategoriesIntent(): Observable<Boolean> {
        return Observable.just(true)
    }

    override fun render(state: SplashState) {
        if (state.data != null){
            goMain(state.data)
        }
    }


    private fun goMain(categories: Categories){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(RouterUtils.CATEGORIES_INTENT_KEY, categories)
        startActivity(intent)
        finish()
    }
}