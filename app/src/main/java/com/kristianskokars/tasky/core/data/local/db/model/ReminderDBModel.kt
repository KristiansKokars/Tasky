package com.kristianskokars.tasky.core.data.local.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kristianskokars.tasky.core.data.remote.model.ReminderResponseDTO
import com.kristianskokars.tasky.core.domain.model.Reminder
import com.kristianskokars.tasky.core.domain.model.toRemindAtTimeOrDefaultThirtyMinutesBefore
import com.kristianskokars.tasky.feature.agenda.domain.model.Agenda
import com.kristianskokars.tasky.lib.toEpochMilliseconds
import com.kristianskokars.tasky.lib.toLocalDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

const val REMINDER_TABLE_NAME = "reminder"

@Entity(tableName = REMINDER_TABLE_NAME)
data class ReminderDBModel(
    @PrimaryKey val id: String,
    val title: String,
    val description: String? = null,
    val timeInMillis: Long,
    val remindAtInMillis: Long
)

fun ReminderDBModel.toReminder() = Reminder(
    id = id,
    title = title,
    description = description ?: "",
    dateTime = timeInMillis.toLocalDateTime(),
    remindAtTime = (timeInMillis - remindAtInMillis).toRemindAtTimeOrDefaultThirtyMinutesBefore(),
)

fun Reminder.toReminderDBModel() = ReminderDBModel(
    id = id,
    title = title,
    description = description,
    timeInMillis = dateTime.toEpochMilliseconds(),
    remindAtInMillis = dateTime.toInstant(TimeZone.currentSystemDefault()).minus(remindAtTime.toDuration()).toEpochMilliseconds(),
)

fun ReminderDBModel.toAgendaReminder(clock: Clock) = Agenda.Reminder(
    id = id,
    title = title,
    atTime = timeInMillis,
    description = description ?: "",
    isInThePast = clock.now().toEpochMilliseconds() > timeInMillis
)

fun ReminderResponseDTO.toDBModel() = ReminderDBModel(
    id = id,
    title = title,
    timeInMillis = time,
    description = description,
    remindAtInMillis = remindAt
)
