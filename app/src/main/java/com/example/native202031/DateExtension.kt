package com.example.native202031

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Date.toBestString(): String {
    kotlin.runCatching {
        val locale = Locale.getDefault()
        val cal = Calendar.getInstance(locale).apply { time = this@toBestString }
        val pattern = DateFormat.getBestDateTimePattern(locale, "yyyyMMMdEEEHHmmss")
        SimpleDateFormat(pattern, locale).format(cal.time)
    }.onSuccess {
        return it
    }.onFailure {
        return it.message ?: ""
    }
    return ""
}