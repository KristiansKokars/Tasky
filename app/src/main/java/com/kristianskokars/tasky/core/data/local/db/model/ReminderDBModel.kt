package com.kristianskokars.tasky.core.data.local.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kristianskokars.tasky.core.data.remote.model.ReminderResponseDTO
import com.kristianskokars.tasky.feature.agenda.data.model.Agenda

const val REMINDER_TABLE_NAME = "reminder"

@Entity(tableName = REMINDER_TABLE_NAME)
data class ReminderDBModel(
    @PrimaryKey val id: String,
    val title: String,
    val description: String? = null,
    val timeInMillis: Long,
    val remindAtInMillis: Long
)

fun ReminderDBModel.toAgendaReminder() = Agenda.Reminder(
    id = id,
    title = title,
    time = timeInMillis,
    description = description ?: "",
    isDone = false
)

fun ReminderResponseDTO.toDBModel() = ReminderDBModel(
    id = id,
    title = title,
    timeInMillis = time,
    description = description,
    remindAtInMillis = remindAt
)
