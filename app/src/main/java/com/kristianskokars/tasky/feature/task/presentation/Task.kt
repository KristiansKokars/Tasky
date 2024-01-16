package com.kristianskokars.tasky.feature.task.presentation

import com.kristianskokars.tasky.core.data.local.model.RemindAtTime
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val dateTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val remindAtTime: RemindAtTime = RemindAtTime.ThirtyMinutesBefore,
    val isDone: Boolean = false,
) {
    companion object {
        fun previewData() = Task(
            title = "Finish app",
            description = "Make the Tasky app",
        )
    }
}
