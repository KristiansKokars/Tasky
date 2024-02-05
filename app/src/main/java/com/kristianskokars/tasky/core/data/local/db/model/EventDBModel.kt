package com.kristianskokars.tasky.core.data.local.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kristianskokars.tasky.core.data.remote.model.AttendeeResponseDTO
import com.kristianskokars.tasky.core.data.remote.model.EventResponseDTO
import com.kristianskokars.tasky.core.domain.model.Event
import com.kristianskokars.tasky.core.domain.model.Photo
import com.kristianskokars.tasky.core.domain.model.toRemindAtTimeOrDefaultThirtyMinutesBefore
import com.kristianskokars.tasky.feature.agenda.domain.model.Agenda
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
import com.kristianskokars.tasky.lib.toEpochMilliseconds
import com.kristianskokars.tasky.lib.toLocalDateTime
import kotlinx.datetime.Clock
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
    val photos: List<Photo>,
    val attendees: List<Attendee>,
)

fun EventDBModel.toEvent() = Event(
    id = id,
    title = title,
    description = description,
    photos = photos,
    fromDateTime = fromInMillis.toLocalDateTime(),
    toDateTime = toInMillis.toLocalDateTime(),
    remindAtTime = (fromInMillis - remindAtInMillis).toRemindAtTimeOrDefaultThirtyMinutesBefore(),
    creatorUserId = host,
    attendees = attendees,
    isUserEventCreator = isUserEventCreator
)

fun Event.toEventDBModel(currentUserId: String) = EventDBModel(
    id = id,
    title = title,
    description = description,
    fromInMillis = fromDateTime.toEpochMilliseconds(),
    toInMillis = toDateTime.toEpochMilliseconds(),
    remindAtInMillis = fromDateTime.toInstant(TimeZone.currentSystemDefault()).minus(remindAtTime.toDuration()).toEpochMilliseconds(),
    host = creatorUserId,
    isUserEventCreator = currentUserId == creatorUserId,
    photos = photos.map { it.copy(key = it.key, url = it.url) },
    attendees = attendees,
)

fun EventDBModel.toAgendaEvent(clock: Clock) = Agenda.Event(
    id = id,
    title = title,
    atTime = fromInMillis,
    toTime = toInMillis,
    description = description,
    isInThePast = clock.now().toEpochMilliseconds() > toInMillis
)

fun EventResponseDTO.toDBModel() = EventDBModel(
    id = id,
    title = title,
    fromInMillis = from,
    toInMillis = to,
    description = description,
    remindAtInMillis = remindAt,
    host = host,
    isUserEventCreator = isUserEventCreator,
    photos = photos.map { Photo(key = it.key, url = it.url, isLocal = false) },
    attendees = attendees.map { it.toAttendee() }
)

fun AttendeeResponseDTO.toAttendee() = Attendee(
    userId = userId,
    email = email,
    fullName = fullName,
    eventId = eventId,
    isGoing = isGoing,
)
