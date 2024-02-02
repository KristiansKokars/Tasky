package com.kristianskokars.tasky.feature.reminder.domain.model

import com.kristianskokars.tasky.core.data.local.model.RemindAtTime
import com.kristianskokars.tasky.lib.randomID
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Reminder(
    val id: String = randomID(),
    val title: String = "New Reminder",
    val description: String = "New Reminder description",
    val dateTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val remindAtTime: RemindAtTime = RemindAtTime.ThirtyMinutesBefore,
)
