package com.kristianskokars.tasky.core.data

import android.net.Uri
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.kristianskokars.tasky.core.data.local.db.EventDao
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.core.data.remote.model.EventRequestDTO
import com.kristianskokars.tasky.core.domain.DeepLinks
import com.kristianskokars.tasky.core.domain.Scheduler
import com.kristianskokars.tasky.core.domain.model.APIError
import com.kristianskokars.tasky.feature.event.domain.PhotoConverter
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
import com.kristianskokars.tasky.lib.Success
import com.kristianskokars.tasky.lib.randomID
import com.kristianskokars.tasky.lib.success
import kotlinx.datetime.Clock
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val clock: Clock,
    private val local: EventDao,
    private val remote: TaskyAPI,
    private val photoConverter: PhotoConverter,
    private val scheduler: Scheduler
) {
    suspend fun saveEvent(
        title: String,
        description: String,
        from: Long,
        to: Long,
        remindAt: Long,
        attendeeIds: List<String>,
        photos: List<Uri>
    ) {
        val id = randomID()
        remote.createEvent(
            createEventRequest = EventRequestDTO(
                id = id,
                title = title,
                description = description,
                from = from,
                to = to,
                remindAt = remindAt,
                attendeeIds = attendeeIds,
            ),
            photos = photoConverter.photosToMultipart(photos)
        )

        if (remindAt > clock.now().toEpochMilliseconds()) {
            scheduler.scheduleExactAlarmAt(
                remindAt,
                id,
                extras = mapOf(
                    DeepLinks.Type.EVENT.toPair(),
                    DeepLinks.Extra.NAME.toString() to title,
                    DeepLinks.Extra.TIME.toString() to remindAt
                )
            )
        }
    }

    suspend fun deleteEvent(eventId: String): Result<Success, APIError> {
        try {
            remote.deleteEvent(eventId)
            local.deleteEvent(eventId)
            scheduler.cancelAlarm(eventId)
        } catch (e: HttpException) {
            return Err(APIError.ServerError)
        } catch (e: IOException) {
            return Err(APIError.ClientError)
        }

        return success()
    }

    suspend fun getAttendee(email: String): Result<Attendee, Unit> {
        val response = remote.getAttendee(email)
        if (!response.doesUserExist || response.attendee == null) return Err(Unit)

        return Ok(
            Attendee(
                userId = response.attendee.userId,
                email = response.attendee.email,
                fullName = response.attendee.fullName,
            )
        )
    }
}
