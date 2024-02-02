package com.kristianskokars.tasky.feature.event.presentation.event

import android.net.Uri
import com.kristianskokars.tasky.core.domain.model.RemindAtTime
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
import com.kristianskokars.tasky.lib.currentSystemDate
import com.kristianskokars.tasky.lib.currentSystemDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class EventState(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "Meeting",
    val description: String? = null,
    val isEditing: Boolean = false,
    val photos: List<Uri> = emptyList(),
    val fromDateTime: LocalDateTime = currentSystemDateTime(),
    val toDateTime: LocalDateTime = currentSystemDateTime(),
    val remindAtTime: RemindAtTime = RemindAtTime.ThirtyMinutesBefore,
    val creator: Attendee? = null,
    val attendees: List<Attendee> = emptyList(),
    val isCheckingIfAttendeeExists: Boolean = false,
    val errorAttendeeDoesNotExist: Boolean = false,
    val currentDate: LocalDate = currentSystemDate()
)
