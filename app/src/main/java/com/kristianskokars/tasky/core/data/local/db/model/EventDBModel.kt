package com.kristianskokars.tasky.core.data.local.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kristianskokars.tasky.core.data.remote.model.EventResponseDTO
import com.kristianskokars.tasky.feature.agenda.domain.model.Agenda

const val EVENT_TABLE_NAME = "event"

@Entity(tableName = EVENT_TABLE_NAME)
data class EventDBModel(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val fromInMillis: Long,
    val toInMillis: Long,
    val remindAtInMillis: Long,
    val host: String,
    val isUserEventCreator: Boolean,
)

fun EventDBModel.toAgendaEvent() = Agenda.Event(
    id = id,
    title = title,
    time = fromInMillis,
    description = description,
    isDone = false
)

fun EventResponseDTO.toDBModel() = EventDBModel(
    id = id,
    title = title,
    fromInMillis = from,
    toInMillis = to,
    description = description,
    remindAtInMillis = remindAt,
    host = host,
    isUserEventCreator = isUserEventCreator
)
