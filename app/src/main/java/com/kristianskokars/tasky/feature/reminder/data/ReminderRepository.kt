package com.kristianskokars.tasky.feature.reminder.data

import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.core.data.remote.model.CreateReminderRequestDTO
import com.kristianskokars.tasky.feature.reminder.domain.model.Reminder
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(
    private val timeZone: TimeZone,
    private val remote: TaskyAPI,
) {
    suspend fun saveReminder(reminder: Reminder) {
        // TODO: offline first and create workmanager task to sync!
        // TODO: error handling
        val time = reminder.dateTime.toInstant(timeZone)
        remote.createReminder(
            CreateReminderRequestDTO(
                id = reminder.id,
                title = reminder.title,
                description = reminder.description,
                time = time.toEpochMilliseconds(),
                remindAt = time.minus(reminder.remindAtTime.toDuration()).toEpochMilliseconds(),
            )
        )
    }

    suspend fun deleteReminder(reminderId: String) {
        // TODO: error handling
        remote.deleteReminder(reminderId)
    }
}