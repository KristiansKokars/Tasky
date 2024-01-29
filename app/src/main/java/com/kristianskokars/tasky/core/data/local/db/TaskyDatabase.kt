package com.kristianskokars.tasky.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kristianskokars.tasky.core.data.local.db.model.TaskDBModel

@Database(entities = [TaskDBModel::class], version = 1)
abstract class TaskyDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
