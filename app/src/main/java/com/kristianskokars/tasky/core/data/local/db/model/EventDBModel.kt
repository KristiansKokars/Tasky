package com.kristianskokars.tasky.core.data.local.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kristianskokars.tasky.core.data.remote.model.EventResponseDTO
import com.kristianskokars.tasky.core.domain.model.Event
import com.kristianskokars.tasky.core.domain.model.toRemindAtTimeOrThrow
import com.kristianskokars.tasky.feature.agenda.domain.model.Agenda
import com.kristianskokars.tasky.lib.toEpochMilliseconds
import com.kristianskokars.tasky.lib.toLocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

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

fun EventDBModel.toEvent() = Event(
    id = id,
    title = title,
    description = description,
    photos = emptyList(), // TODO: deal with photos and creator here, figure out if it needs another table?
    fromDateTime = fromInMillis.toLocalDateTime(),
    toDateTime = toInMillis.toLocalDateTime(),
    remindAtTime = (fromInMillis - remindAtInMillis).toRemindAtTimeOrThrow(),
    creator = null,
    attendees = emptyList()
)

fun Event.toEventDBModel(currentUserId: String) = EventDBModel(
    id = id,
    title = title,
    description = description,
    fromInMillis = fromDateTime.toEpochMilliseconds(),
    toInMillis = toDateTime.toEpochMilliseconds(),
    remindAtInMillis = fromDateTime.toInstant(TimeZone.currentSystemDefault()).minus(remindAtTime.toDuration()).toEpochMilliseconds(),
    host = creator?.userId ?: "",
    isUserEventCreator = currentUserId == creator?.userId
)

fun EventDBModel.toAgendaEvent() = Agenda.Event(
    id = id,
    title = title,
    atTime = fromInMillis,
    toTime = toInMillis,
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
