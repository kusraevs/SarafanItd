package ru.itd.sarafan.rest

import android.content.Context
import io.reactivex.Observable
import ru.itd.sarafan.rest.model.editors.Editor
import ru.itd.sarafan.rest.model.editors.EditorsGenerator

/**
 * Created by macbook on 28.11.2017.
 */
class EditorsLoader(val context: Context) {
    fun loadEditors(): Observable<List<Editor>> = Observable.just(EditorsGenerator.generateFromResources(context))
}