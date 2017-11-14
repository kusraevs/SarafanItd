package ru.itd.sarafan.utils

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by macbook on 14.11.17.
 */
class DateUtils {
    companion object {
        private val inputDatePattern: String = "yyyy-MM-dd'T'HH:mm"
        private val outputDatePattern: String = "dd MMMM, yyyy"
        private val inputFormat = SimpleDateFormat(inputDatePattern, Locale.ENGLISH)
        private val outputFormat = SimpleDateFormat(outputDatePattern, Locale.getDefault())


        fun formatDate(date: String): String {
            val calendar = Calendar.getInstance()
            calendar.time = inputFormat.parse(date)
            return outputFormat.format(inputFormat.parse(date))
        }
    }
}