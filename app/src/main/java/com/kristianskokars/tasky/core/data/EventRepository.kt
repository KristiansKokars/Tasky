package com.kristianskokars.tasky.core.data

import androidx.datastore.core.DataStore
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.kristianskokars.tasky.core.data.local.db.EventDao
import com.kristianskokars.tasky.core.data.local.db.model.toEvent
import com.kristianskokars.tasky.core.data.local.db.model.toEventDBModel
import com.kristianskokars.tasky.core.data.local.model.UserSettings
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.core.data.remote.model.EventRequestDTO
import com.kristianskokars.tasky.core.data.remote.model.UpdateEventRequestDTO
import com.kristianskokars.tasky.core.domain.DeepLinks
import com.kristianskokars.tasky.core.domain.Scheduler
import com.kristianskokars.tasky.core.domain.model.APIError
import com.kristianskokars.tasky.core.domain.model.Event
import com.kristianskokars.tasky.feature.event.domain.PhotoConverter
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
import com.kristianskokars.tasky.lib.Success
import com.kristianskokars.tasky.lib.success
import com.kristianskokars.tasky.lib.toEpochMilliseconds
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val clock: Clock,
    private val timeZone: TimeZone,
    private val local: EventDao,
    private val remote: TaskyAPI,
    private val photoConverter: PhotoConverter,
    private val scheduler: Scheduler,
    private val userStore: DataStore<UserSettings>
) {
    fun getEvent(eventId: String) = local.getEvent(eventId).map { it?.toEvent() }

    suspend fun saveEvent(event: Event): Result<Success, APIError> {
        val existingEvent = local.getEvent(event.id).first()
        val time = event.fromDateTime.toInstant(timeZone)
        val remindAtInMillis = time.minus(event.remindAtTime.toDuration()).toEpochMilliseconds()
        val currentUserId = userStore.data.first().userId ?: return Err(APIError.ClientError)

        try {
            if (existingEvent == null) {
                remote.createEvent(
                    createEventRequest = EventRequestDTO(
                        id = event.id,
                        title = event.title,
                        description = event.description,
                        from = event.fromDateTime.toEpochMilliseconds(),
                        to = event.toDateTime.toEpochMilliseconds(),
                        remindAt = remindAtInMillis,
                        attendeeIds = event.attendees.map { it.userId },
                    ),
                    photos = photoConverter.photosToMultipart(event.photos)
                )
            } else {
                val photosToAdd = event.photos.filter { it !in existingEvent.photos && it.isLocal }
                val photosToRemove = existingEvent.photos.filter { it !in event.photos }
                remote.updateEvent(
                    updateEventRequest = UpdateEventRequestDTO(
                        id = event.id,
                        title = event.title,
                        description = event.description,
                        from = event.fromDateTime.toEpochMilliseconds(),
                        to = event.toDateTime.toEpochMilliseconds(),
                        remindAt = remindAtInMillis,
                        attendeeIds = event.attendees.map { it.userId },
                        deletedPhotoKeys = photosToRemove.map { it.key },
                        isGoing = true // TODO: create functionality
                    ),
                    photos = photoConverter.photosToMultipart(photosToAdd)
                )
            }
        } catch (e: HttpException) {
            return Err(APIError.ServerError)
        } catch (e: IOException) {
            return Err(APIError.ClientError)
        }

        withContext(NonCancellable) {
            local.insertEvent(event.toEventDBModel(currentUserId))

            if (
                remindAtInMillis > clock.now().toEpochMilliseconds() &&
                existingEvent?.remindAtInMillis != remindAtInMillis
            ) {
                scheduler.scheduleExactAlarmAt(
                    remindAtInMillis,
                    event.id,
                    extras = mapOf(
                        DeepLinks.Type.EVENT.toPair(),
                        DeepLinks.Extra.NAME.toString() to event.title,
                        DeepLinks.Extra.TIME.toString() to time.toEpochMilliseconds()
                    )
                )
            }
        }

        return success()
    }

    suspend fun deleteEvent(eventId: String): Result<Success, APIError> {
        try {
            val existingEvent = local.getEvent(eventId).first() ?: return Err(APIError.ClientError)

            if (existingEvent.isUserEventCreator) {
                remote.deleteEvent(eventId)
            } else {
                remote.deleteAttendee(eventId)
            }

            withContext(NonCancellable) {
                local.deleteEvent(eventId)
                scheduler.cancelAlarm(eventId)
            }
        } catch (e: HttpException) {
            return Err(APIError.ServerError)
        } catch (e: IOException) {
            return Err(APIError.ClientError)
        }

        return success()
    }

    suspend fun getAttendee(email: String, eventId: String): Result<Attendee, Unit> {
        val response = remote.getAttendee(email)
        if (!response.doesUserExist || response.attendee == null) return Err(Unit)

        return Ok(
            Attendee(
                userId = response.attendee.userId,
                email = response.attendee.email,
                fullName = response.attendee.fullName,
                eventId = eventId,
                isGoing = true,
            )
        )
    }
}
