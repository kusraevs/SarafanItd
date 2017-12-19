package ru.itd.sarafan.rest.model.editors

import android.content.Context
import android.support.v4.content.ContextCompat
import ru.itd.sarafan.R

/**
 * Created by macbook on 28.11.2017.
 */
class EditorsGenerator {

    companion object {
        fun generateFromResources(context: Context): List<Editor> {
            val editors = ArrayList<Editor>()
            editors.add(Editor(context.getString(R.string.third_author),
                    context.getString(R.string.third_author_title),
                    ContextCompat.getDrawable(context, R.drawable.olga)))

            editors.add(Editor(context.getString(R.string.first_author),
                    context.getString(R.string.first_author_title),
                    ContextCompat.getDrawable(context, R.drawable.nadya)))

            editors.add(Editor(context.getString(R.string.second_author),
                    context.getString(R.string.second_author_title),
                    ContextCompat.getDrawable(context, R.drawable.kate)))

            editors.add(Editor(context.getString(R.string.fourth_author),
                    context.getString(R.string.fourth_author_title),
                    ContextCompat.getDrawable(context, R.drawable.lera)))

            editors.add(Editor(context.getString(R.string.fifth_author),
                    context.getString(R.string.fifth_author_title),
                    ContextCompat.getDrawable(context, R.drawable.igor)))

            editors.add(Editor(context.getString(R.string.sixth_author),
                    context.getString(R.string.sixth_author_title),
                    ContextCompat.getDrawable(context, R.drawable.lisa)))

            return editors
        }
    }

}