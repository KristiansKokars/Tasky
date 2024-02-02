package com.kristianskokars.tasky.lib

import android.os.Build
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalTime
import kotlinx.datetime.toLocalDateTime
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

/** Formats a date like: 01 March 2022 */
fun LocalDate.formatToLongDate(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    return toJavaLocalDate().format(dateFormatter)
}

/** Formats a date like: 01 MARCH 2022 */
fun LocalDate.formatToLongDateUppercase(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    return toJavaLocalDate().format(dateFormatter).uppercase()
}

/** Formats a date like: 08:30 */
fun LocalDateTime.formatToHHMM(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return time.toJavaLocalTime().format(dateFormatter)
}

fun LocalDateTime.formatToDateWithHHMM(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("MMMM d HH:mm")
    return toJavaLocalDateTime().format(dateFormatter)
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

fun LocalDate.nameOfMonth(locale: Locale): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        month.getDisplayName(TextStyle.FULL, locale)
    } else {
        SimpleDateFormat("MMMM", locale).format(Calendar.getInstance(locale).get(monthNumber))
    }
}

fun currentSystemDate() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun currentSystemDateTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDate.next6Days(): List<LocalDate> = List(6) { index ->
    this.plus(index, DateTimeUnit.DAY)
}

fun Long.toLocalDateTime() = Instant.fromEpochMilliseconds(this).toLocalDateTime(
    TimeZone.currentSystemDefault()
)

fun allTimesOfDay() = java.time.LocalTime.MIN.toKotlinLocalTime() .. localTimeMax()

fun localTimeMax() = java.time.LocalTime.MAX.toKotlinLocalTime()

fun Clock.currentDate(timeZone: TimeZone) = now().toLocalDateTime(timeZone).date

fun Clock.currentTime(timeZone: TimeZone) = now().toLocalDateTime(timeZone).time
