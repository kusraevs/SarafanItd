package ru.itd.sarafan.businesslogic

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/**
 * Created by Kusraev Soslan on 11/7/17.
 */
open class SinglePropertyManager<T> {
    private val updatePublisher = PublishSubject.create<T>()

    fun change(value: T): Observable<T> {
        updatePublisher.onNext(value)
        return Observable.just(value)
    }

    fun subscribeToUpdates(): Observable<T> {
        return updatePublisher
    }

}