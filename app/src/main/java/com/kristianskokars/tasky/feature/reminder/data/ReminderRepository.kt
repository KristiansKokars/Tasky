package com.kristianskokars.tasky.feature.reminder.data

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.kristianskokars.tasky.core.data.local.db.ReminderDao
import com.kristianskokars.tasky.core.data.local.db.model.toReminder
import com.kristianskokars.tasky.core.data.local.db.model.toReminderDBModel
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.core.data.remote.model.CreateReminderRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.UpdateReminderRequestDTO
import com.kristianskokars.tasky.core.domain.APIError
import com.kristianskokars.tasky.core.domain.DeepLinks
import com.kristianskokars.tasky.core.domain.Scheduler
import com.kristianskokars.tasky.feature.reminder.domain.model.Reminder
import com.kristianskokars.tasky.lib.Success
import com.kristianskokars.tasky.lib.success
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(
    private val timeZone: TimeZone,
    private val clock: Clock,
    private val local: ReminderDao,
    private val remote: TaskyAPI,
    private val scheduler: Scheduler,
) {
    fun getReminder(reminderId: String) = local.getReminder(reminderId).map { it?.toReminder() }

    suspend fun saveReminder(reminder: Reminder): Result<Success, APIError> {
        // TODO: offline first and create workmanager task to sync!
        val existingReminder = local.getReminder(reminder.id).first()
        val time = reminder.dateTime.toInstant(timeZone)
        val remindAtInMillis = time.minus(reminder.remindAtTime.toDuration()).toEpochMilliseconds()

        try {
            if (existingReminder == null) {
                remote.createReminder(
                    CreateReminderRequestDTO(
                        id = reminder.id,
                        title = reminder.title,
                        description = reminder.description,
                        time = time.toEpochMilliseconds(),
                        remindAt = remindAtInMillis,
                    )
                )
            } else {
                remote.updateReminder(
                    UpdateReminderRequestDTO(
                        id = reminder.id,
                        title = reminder.title,
                        description = reminder.description,
                        time = time.toEpochMilliseconds(),
                        remindAt = remindAtInMillis,
                    )
                )
            }
        } catch (e: HttpException) {
            return Err(APIError.ServerError)
        } catch (e: IOException) {
            return Err(APIError.ClientError)
        }

        local.insertReminder(reminder.toReminderDBModel())

        if (
            remindAtInMillis > clock.now().toEpochMilliseconds() &&
            existingReminder?.remindAtInMillis != remindAtInMillis
        ) {
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

        return success()
    }

    suspend fun deleteReminder(reminderId: String): Result<Success, APIError> {
        try {
            remote.deleteReminder(reminderId)
            local.deleteReminder(reminderId)
            scheduler.cancelAlarm(reminderId)
        } catch (e: HttpException) {
            return Err(APIError.ServerError)
        } catch (e: IOException) {
            return Err(APIError.ClientError)
        }

        return success()
    }
}
