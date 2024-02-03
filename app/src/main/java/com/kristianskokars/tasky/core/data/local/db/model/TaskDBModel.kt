package com.kristianskokars.tasky.core.data.local.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kristianskokars.tasky.core.data.remote.model.TaskResponseDTO
import com.kristianskokars.tasky.core.domain.model.Task
import com.kristianskokars.tasky.core.domain.model.toRemindAtTimeOrThrow
import com.kristianskokars.tasky.feature.agenda.domain.model.Agenda
import com.kristianskokars.tasky.lib.toLocalDateTime
import kotlinx.datetime.Clock

const val TASK_TABLE_NAME = "task"

@Entity(tableName = TASK_TABLE_NAME)
data class TaskDBModel(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val timeInMillis: Long,
    val remindAtTimeInMillis: Long,
    val isDone: Boolean,
)

fun TaskDBModel.toTask() = Task(
    id = id,
    title = title,
    description = description,
    dateTime = timeInMillis.toLocalDateTime(),
    remindAtTime = (timeInMillis - remindAtTimeInMillis).toRemindAtTimeOrThrow(),
    isDone = isDone
)

fun TaskDBModel.toAgendaTask(clock: Clock) = Agenda.Task(
    id = id,
    title = title,
    atTime = timeInMillis,
    description = description,
    isDone = isDone,
    isInThePast = clock.now().toEpochMilliseconds() > timeInMillis
)

fun TaskResponseDTO.toDBModel() = TaskDBModel(
    id = id,
    title = title,
    timeInMillis = time,
    description = description ?: "",
    remindAtTimeInMillis = remindAt,
    isDone = isDone
)
