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
            editors.add(Editor(context.getString(R.string.first_author),
                    context.getString(R.string.first_author_title),
                    ContextCompat.getDrawable(context, R.drawable.nadia)))

            editors.add(Editor(context.getString(R.string.second_author),
                    context.getString(R.string.second_author_title),
                    ContextCompat.getDrawable(context, R.drawable.kate)))

            editors.add(Editor(context.getString(R.string.third_author),
                    context.getString(R.string.third_author_title),
                    ContextCompat.getDrawable(context, R.drawable.olga)))
            return editors
        }
    }

}