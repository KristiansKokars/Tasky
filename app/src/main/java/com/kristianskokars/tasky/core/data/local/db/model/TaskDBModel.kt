package com.kristianskokars.tasky.core.data.local.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kristianskokars.tasky.core.data.remote.model.TaskResponseDTO
import com.kristianskokars.tasky.feature.agenda.data.model.Agenda

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

fun TaskDBModel.toAgendaTask() = Agenda.Task(
    id = id,
    title = title,
    time = timeInMillis,
    description = description,
    isDone = isDone
)

fun TaskResponseDTO.toDBModel() = TaskDBModel(
    id = id,
    title = title,
    timeInMillis = time,
    description = description ?: "",
    remindAtTimeInMillis = remindAt,
    isDone = isDone
)
