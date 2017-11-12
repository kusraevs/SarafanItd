package ru.itd.sarafan.rest.interactors

import io.reactivex.Observable
import ru.itd.sarafan.rest.model.Categories

/**
 * Created by macbook on 23.10.17.
 */
open class GetInteractor<T>(private val data: T?) {

    fun execute(): Observable<T> {
        if (data == null)
            return Observable.empty()
        return Observable.just(data)
    }


}