package com.kristianskokars.tasky.feature.agenda.presentation.util

import android.os.Build
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import java.text.SimpleDateFormat
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Long.formatTime(): String {
    val currentLocale = Locale.getDefault()
    val dateFormatter = SimpleDateFormat("EEE d, HH:mm", currentLocale)
    return dateFormatter.format(Date(this))
}

fun DayOfWeek.initial(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        getDisplayName(TextStyle.SHORT, Locale.getDefault())[0].toString()
    } else {
        name[0].toString()
    }
}

fun LocalDateTime.nameOfMonth(locale: Locale): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        month.getDisplayName(TextStyle.FULL, locale)
    } else {
        SimpleDateFormat("MMMM", locale).format(Calendar.getInstance(locale).get(monthNumber))
    }
}
