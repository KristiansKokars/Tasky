package com.kristianskokars.tasky.core.data

import com.kristianskokars.tasky.core.data.local.db.TaskDao
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.core.data.remote.model.CreateTaskRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.UpdateTaskRequestDTO
import com.kristianskokars.tasky.core.domain.DeepLinks
import com.kristianskokars.tasky.core.domain.Scheduler
import com.kristianskokars.tasky.feature.task.domain.model.Task
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val timeZone: TimeZone,
    private val clock: Clock,
    private val remote: TaskyAPI,
    private val local: TaskDao,
    private val scheduler: Scheduler
) {
    suspend fun saveTask(task: Task) {
        // TODO: offline first and create workmanager task to sync!
        // TODO: error handling
        val time = task.dateTime.toInstant(timeZone)
        val remindAtInMillis = time.minus(task.remindAtTime.toDuration()).toEpochMilliseconds()
        remote.createTask(
            CreateTaskRequestDTO(
                id = task.id,
                title = task.title,
                description = task.description,
                time = time.toEpochMilliseconds(),
                remindAt = remindAtInMillis,
                isDone = task.isDone,
            )
        )

        if (remindAtInMillis > clock.now().toEpochMilliseconds()) {
            scheduler.scheduleExactAlarmAt(
                remindAtInMillis,
                task.id,
                extras = mapOf(
                    DeepLinks.Type.TASK.toPair(),
                    DeepLinks.Extra.NAME.toString() to task.title,
                    DeepLinks.Extra.TIME.toString() to time.toEpochMilliseconds()
                )
            )
        }
    }

    suspend fun deleteTask(taskId: String) {
        // TODO: error handling
        remote.deleteTask(taskId)
        scheduler.cancelAlarm(taskId)
    }

    suspend fun markTaskAsDone(taskId: String) {
        // TODO: error handling
        val task = local.getTask(taskId)

        remote.updateTask(
            UpdateTaskRequestDTO(
                id = task.id,
                title = task.title,
                description = task.description,
                time = task.timeInMillis,
                remindAt = task.remindAtTimeInMillis,
                isDone = !task.isDone
            )
        )
        local.insertTask(task.copy(isDone = !task.isDone))
    }
}