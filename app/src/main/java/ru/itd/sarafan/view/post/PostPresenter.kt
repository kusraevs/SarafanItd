package ru.itd.sarafan.view.post

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.rest.model.Post
import ru.itd.sarafan.utils.RouterUtils
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by macbook on 12.11.17.
 */
class PostPresenter(val post: Post): MviBasePresenter<PostView, PostViewState>() {
    @Inject
    protected lateinit var router: Router

    init {
        SarafanApplication.getComponent().inject(this)
    }

    override fun bindIntents() {
        val startIntentObservable = intent(PostView::startIntent)
                .map { PostViewState(post) }

        val shareClickObservable = intent(PostView::shareClickIntent)
                .doOnNext {
                    val shareTitle = StringBuilder().append(post.title?.rendered).append("\n").append(post.link).toString()
                    router.navigateTo(RouterUtils.SHARE_SCREEN, shareTitle)
                }
                .flatMap { Observable.empty<PostViewState>() }

        val allIntents = Observable.merge(startIntentObservable, shareClickObservable)

        subscribeViewState(allIntents, PostView::render)
    }
}