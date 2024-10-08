package com.kristianskokars.tasky.core.domain.model

import com.kristianskokars.tasky.lib.randomID
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Task(
    val id: String = randomID(),
    val title: String = "New Task",
    val description: String = "Task description",
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
