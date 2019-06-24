package com.example.moviesapp.util

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun getYearFromDate(date: String?): String {
        return if (date.isNullOrEmpty()) {
            "-"
        } else {
            try {
                val apiDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val apiDate = apiDateFormatter.parse(date)
                val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())
                yearFormatter.format(apiDate)
            } catch (ex: ParseException) {
                Timber.d(ex, "Cannot parse $date")
                "-"
            }
        }
    }
}