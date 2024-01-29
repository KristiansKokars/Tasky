package com.kristianskokars.tasky.feature.event.data

import android.net.Uri
import com.kristianskokars.tasky.core.data.remote.TaskyAPI
import com.kristianskokars.tasky.core.data.remote.model.EventRequestDTO
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
import com.kristianskokars.tasky.feature.event.presentation.PhotoConverter
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val api: TaskyAPI,
    private val photoConverter: PhotoConverter
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
        api.createEvent(
            createEventRequest = EventRequestDTO(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                from = from,
                to = to,
                remindAt = remindAt,
                attendeeIds = attendeeIds,
            ),
            photos = photoConverter.photosToMultipart(photos)
        )
    }

    suspend fun deleteEvent(eventId: String) {
        api.deleteEvent(eventId)
    }

    suspend fun getAttendee(email: String): Attendee? {
        val response = api.getAttendee(email)
        if (!response.doesUserExist || response.attendee == null) return null

        return Attendee(
            userId = response.attendee.userId,
            email = response.attendee.email,
            fullName = response.attendee.fullName,
        )
    }
}