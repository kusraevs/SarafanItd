package ru.itd.sarafan.businesslogic.interactors

import io.reactivex.Observable
import java.util.*

/**
 * Created by macbook on 23.10.17.
 */
class GetWeekTypeInteractor {
    fun execute(): Observable<Boolean> {
        val isDenominator = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) % 2 == 1
        return Observable.just(isDenominator)
    }
}