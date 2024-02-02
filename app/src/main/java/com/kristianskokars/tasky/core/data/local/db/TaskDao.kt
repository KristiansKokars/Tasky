package com.kristianskokars.tasky.core.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kristianskokars.tasky.core.data.local.db.model.TASK_TABLE_NAME
import com.kristianskokars.tasky.core.data.local.db.model.TaskDBModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM $TASK_TABLE_NAME WHERE id = :taskId")
    fun getTask(taskId: String): Flow<TaskDBModel?>

    @Query("SELECT * FROM $TASK_TABLE_NAME WHERE timeInMillis > :startingDayMillis AND timeInMillis < :endingDayMillis")
    fun getTasksForDay(startingDayMillis: Long, endingDayMillis: Long): Flow<List<TaskDBModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskDBModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskDBModel)

    @Query("DELETE FROM $TASK_TABLE_NAME WHERE id = :taskId")
    suspend fun deleteTask(taskId: String)
}
