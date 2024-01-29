package com.kristianskokars.tasky.core.data

import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.core.data.remote.model.CreateTaskRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.UpdateTaskRequestDTO
import com.kristianskokars.tasky.feature.task.presentation.Task
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val timeZone: TimeZone,
    private val remote: TaskyAPI
) {
    suspend fun saveTask(task: Task) {
        // TODO: offline first and create workmanager task to sync!
        // TODO: error handling
        val time = task.dateTime.toInstant(timeZone)
        remote.createTask(
            CreateTaskRequestDTO(
                id = task.id,
                title = task.title,
                description = task.description,
                time = time.toEpochMilliseconds(),
                remindAt = time.minus(task.remindAtTime.toDuration()).toEpochMilliseconds(),
                isDone = task.isDone,
            )
        )
    }

    suspend fun deleteTask(taskId: String) {
        // TODO: error handling
        remote.deleteTask(taskId)
    }

    suspend fun markTaskAsDone(taskId: String) {
        // TODO: error handling
        val task = remote.getTask(taskId)

        remote.updateTask(
            UpdateTaskRequestDTO(
                id = task.id,
                title = task.title,
                description = task.description,
                time = task.time,
                remindAt = task.remindAt,
                isDone = !task.isDone
            )
        )
    }
}