package com.kristianskokars.tasky.core.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kristianskokars.tasky.R
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlin.time.toDuration

sealed interface RemindAtTime {
    data object TenMinutesBefore : RemindAtTime
    data object ThirtyMinutesBefore : RemindAtTime
    data object OneHourBefore : RemindAtTime
    data object SixHoursBefore : RemindAtTime
    data object OneDayBefore : RemindAtTime

    @Composable
    fun toUIString() = when (this) {
        TenMinutesBefore -> stringResource(R.string.ten_minutes_before)
        ThirtyMinutesBefore -> stringResource(R.string.thirty_minutes_before)
        OneHourBefore -> stringResource(R.string.one_hour_before)
        SixHoursBefore -> stringResource(R.string.six_hours_before)
        OneDayBefore -> stringResource(R.string.one_day_before)
    }

    fun toDuration() = when (this) {
        TenMinutesBefore -> 10.minutes
        ThirtyMinutesBefore -> 30.minutes
        OneHourBefore -> 1.hours
        SixHoursBefore -> 6.hours
        OneDayBefore -> 1.days
    }

    companion object {
        val remindAtTimes = listOf(
            TenMinutesBefore,
            ThirtyMinutesBefore,
            OneHourBefore,
            SixHoursBefore,
            OneDayBefore
        )
    }
}

fun Long.toRemindAtTimeOrThrow(): RemindAtTime {
    val duration = toDuration(DurationUnit.MILLISECONDS)
    return when {
        duration.inWholeMinutes == 10L -> RemindAtTime.TenMinutesBefore
        duration.inWholeMinutes == 30L -> RemindAtTime.TenMinutesBefore
        duration.inWholeHours == 1L -> RemindAtTime.OneHourBefore
        duration.inWholeHours == 6L -> RemindAtTime.SixHoursBefore
        duration.inWholeDays == 1L -> RemindAtTime.OneDayBefore
        else -> throw IllegalStateException("Duration was not a valid remind at time object")
    }
}
