package com.kristianskokars.tasky.core.data

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.kristianskokars.tasky.core.data.local.db.TaskDao
import com.kristianskokars.tasky.core.data.local.db.model.TaskDBModel
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.core.data.remote.model.CreateTaskRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.UpdateTaskRequestDTO
import com.kristianskokars.tasky.core.domain.DeepLinks
import com.kristianskokars.tasky.core.domain.Scheduler
import com.kristianskokars.tasky.core.domain.model.APIError
import com.kristianskokars.tasky.core.domain.model.Task
import com.kristianskokars.tasky.lib.Success
import com.kristianskokars.tasky.lib.success
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import retrofit2.HttpException
import java.io.IOException
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
    fun getTask(taskId: String) = local.getTask(taskId)

    suspend fun saveTask(task: Task): Result<Success, APIError> {
        val existingTask = local.getTask(task.id).first()
        val time = task.dateTime.toInstant(timeZone)
        val remindAtInMillis = time.minus(task.remindAtTime.toDuration()).toEpochMilliseconds()

        try {
            if (existingTask == null) {
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
            } else {
                remote.updateTask(
                    UpdateTaskRequestDTO(
                        id = task.id,
                        title = task.title,
                        description = task.description,
                        time = time.toEpochMilliseconds(),
                        remindAt = remindAtInMillis,
                        isDone = task.isDone,
                    )
                )
            }
        } catch (e: HttpException) {
            return Err(APIError.ServerError)
        } catch (e: IOException) {
            return Err(APIError.ClientError)
        }

        withContext(NonCancellable) {
            local.insertTask(
                TaskDBModel(
                    id = task.id,
                    title = task.title,
                    description = task.description,
                    timeInMillis = time.toEpochMilliseconds(),
                    remindAtTimeInMillis = remindAtInMillis,
                    isDone = task.isDone,
                )
            )

            if (
                remindAtInMillis > clock.now().toEpochMilliseconds() &&
                existingTask?.remindAtTimeInMillis != remindAtInMillis
            ) {
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

        return success()
    }

    suspend fun deleteTask(taskId: String): Result<Success, APIError> {
        try {
            remote.deleteTask(taskId)
            withContext(NonCancellable) {
                local.deleteTask(taskId)
                scheduler.cancelAlarm(taskId)
            }
        } catch (e: HttpException) {
            return Err(APIError.ServerError)
        } catch (e: IOException) {
            return Err(APIError.ClientError)
        }

        return success()
    }

    suspend fun markTaskAsDone(taskId: String): Result<Success, APIError> {
        val task = local.getTask(taskId).first() ?: return Err(APIError.ClientError)

        try {
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
            withContext(NonCancellable) { local.insertTask(task.copy(isDone = !task.isDone)) }
        } catch (e: HttpException) {
            return Err(APIError.ServerError)
        } catch (e: IOException) {
            return Err(APIError.ClientError)
        }

        return success()
    }
}
