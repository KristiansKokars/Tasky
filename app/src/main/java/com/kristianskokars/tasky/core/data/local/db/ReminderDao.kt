package com.kristianskokars.tasky.core.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kristianskokars.tasky.core.data.local.db.model.REMINDER_TABLE_NAME
import com.kristianskokars.tasky.core.data.local.db.model.ReminderDBModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM $REMINDER_TABLE_NAME WHERE id = :reminderId")
    fun getReminder(reminderId: String): Flow<ReminderDBModel?>

    @Query("SELECT * FROM $REMINDER_TABLE_NAME WHERE timeInMillis > :startingDayMillis AND timeInMillis < :endingDayMillis")
    fun getRemindersForDay(startingDayMillis: Long, endingDayMillis: Long): Flow<List<ReminderDBModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<ReminderDBModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderDBModel)

    @Query("DELETE FROM $REMINDER_TABLE_NAME WHERE id = :reminderId")
    suspend fun deleteReminder(reminderId: String)
}
