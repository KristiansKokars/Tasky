package com.kristianskokars.tasky.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kristianskokars.tasky.core.data.local.db.model.EventDBModel
import com.kristianskokars.tasky.core.data.local.db.model.ReminderDBModel
import com.kristianskokars.tasky.core.data.local.db.model.TaskDBModel

@Database(entities = [TaskDBModel::class, EventDBModel::class, ReminderDBModel::class], version = 5)
@TypeConverters(Converters::class)
abstract class TaskyDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun eventDao(): EventDao
    abstract fun reminderDao(): ReminderDao
}
