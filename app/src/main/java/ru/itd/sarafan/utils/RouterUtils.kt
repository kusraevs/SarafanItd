package ru.itd.sarafan.utils

import android.app.Activity
import android.content.Intent
import android.os.Parcelable


/**
 * Created by macbook on 21.10.17.
 */
class RouterUtils {
    companion object {
        val CATEGORIES_INTENT_KEY = "categories"

        val SHARE_SCREEN = "Share"
    }



}

fun Activity.goShare(bodyStr: String) {
    val targetedShareIntents = ArrayList<Intent>()
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "image/jpeg"
    val resInfo = packageManager.queryIntentActivities(intent, 0)
    if (!resInfo.isEmpty()) {
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedShareIntent = Intent(android.content.Intent.ACTION_SEND)
            targetedShareIntent.type = "image/jpeg"
            targetedShareIntent.putExtra(Intent.EXTRA_TITLE, "Title string")
            targetedShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject string")
            targetedShareIntent.putExtra(Intent.EXTRA_TEXT, bodyStr)
            targetedShareIntent.`package` = packageName
            targetedShareIntents.add(targetedShareIntent)
        }
        val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0), "Title")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray<Parcelable>())
        startActivity(chooserIntent)
    }
}