package com.kristianskokars.tasky.core.presentation.util

import android.os.Build
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun LocalDateTime.formatToDate(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d yyyy")
    return toJavaLocalDateTime().format(dateFormatter)
}

fun LocalDateTime.formatToHHMM(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return time.toJavaLocalTime().format(dateFormatter)
}

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
