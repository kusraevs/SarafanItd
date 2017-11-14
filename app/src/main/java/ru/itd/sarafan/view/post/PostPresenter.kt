package ru.itd.sarafan.view.post

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import ru.itd.sarafan.rest.model.Post
/**
 * Created by macbook on 12.11.17.
 */
class PostPresenter(val post: Post): MviBasePresenter<PostView, PostViewState>() {
    override fun bindIntents() {
        val startIntentObservable = intent(PostView::startIntent)
                .map { PostViewState(post) }

        val allIntents = startIntentObservable

        subscribeViewState(allIntents, PostView::render)
    }
}