package com.kristianskokars.tasky.feature.reminder.domain.model

import com.kristianskokars.tasky.core.data.local.model.RemindAtTime
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

data class Reminder(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "Placeholder title",
    val description: String = "Placeholder description",
    val dateTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val remindAtTime: RemindAtTime = RemindAtTime.ThirtyMinutesBefore,
)
