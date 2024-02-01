package com.kristianskokars.tasky.feature.reminder.data

import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.core.data.remote.model.CreateReminderRequestDTO
import com.kristianskokars.tasky.core.domain.DeepLinks
import com.kristianskokars.tasky.core.domain.Scheduler
import com.kristianskokars.tasky.feature.reminder.domain.model.Reminder
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(
    private val timeZone: TimeZone,
    private val clock: Clock,
    private val remote: TaskyAPI,
    private val scheduler: Scheduler,
) {
    suspend fun saveReminder(reminder: Reminder) {
        // TODO: offline first and create workmanager task to sync!
        // TODO: error handling
        val time = reminder.dateTime.toInstant(timeZone)
        val remindAtInMillis = time.minus(reminder.remindAtTime.toDuration()).toEpochMilliseconds()

        remote.createReminder(
            CreateReminderRequestDTO(
                id = reminder.id,
                title = reminder.title,
                description = reminder.description,
                time = time.toEpochMilliseconds(),
                remindAt = remindAtInMillis,
            )
        )

        if (remindAtInMillis > clock.now().toEpochMilliseconds()) {
            scheduler.scheduleExactAlarmAt(
                remindAtInMillis,
                reminder.id,
                extras = mapOf(
                    DeepLinks.Type.REMINDER.toPair(),
                    DeepLinks.Extra.NAME.toString() to reminder.title,
                    DeepLinks.Extra.TIME.toString() to time.toEpochMilliseconds()
                )
            )
        }
    }

    suspend fun deleteReminder(reminderId: String) {
        // TODO: error handling
        remote.deleteReminder(reminderId)
    }
}