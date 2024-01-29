package com.kristianskokars.tasky.core.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kristianskokars.tasky.core.data.local.db.model.EVENT_TABLE_NAME
import com.kristianskokars.tasky.core.data.local.db.model.EventDBModel
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM $EVENT_TABLE_NAME WHERE id = :eventId")
    suspend fun getEvent(eventId: String): EventDBModel

    @Query("SELECT * FROM $EVENT_TABLE_NAME WHERE fromInMillis > :startingDayMillis OR toInMillis < :endingDayMillis")
    fun getEventsForDay(startingDayMillis: Long, endingDayMillis: Long): Flow<List<EventDBModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventDBModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventDBModel)
}