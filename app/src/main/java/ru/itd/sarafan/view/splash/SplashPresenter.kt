package ru.itd.sarafan.view.splash

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.businesslogic.interactors.LoadCategoriesInteractor
import javax.inject.Inject

/**
 * Created by macbook on 23.10.17.
 */
class SplashPresenter: MviBasePresenter<SplashView, SplashState>() {

    @Inject lateinit var loadCategoriesInteractor: LoadCategoriesInteractor

    init {
        SarafanApplication.getComponent().inject(this)
    }

    override fun bindIntents() {
        val loadCategoriesObservable = intent(SplashView::loadCategoriesIntent)
                .flatMap { loadCategoriesInteractor.loadCategories() }
                .onErrorReturn {
                    it.printStackTrace()
                    SplashState(error = it)
                }
                .observeOn(AndroidSchedulers.mainThread())

        subscribeViewState(loadCategoriesObservable, SplashView::render)
    }
}

/*
.observeOn(AndroidSchedulers.mainThread())
.subscribe({ list ->
    rvRootCategories.layoutManager = LinearLayoutManager(applicationContext)
    val adapter = RootCategoriesAdapter(applicationContext, list)
    rvRootCategories.adapter = adapter
    rvRootCategories.adapter.notifyDataSetChanged()
    adapter.clickListener = this
}, Throwable::printStackTrace)


}
}*/