package com.kristianskokars.tasky.core.domain.model

import android.net.Uri
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
import com.kristianskokars.tasky.lib.currentSystemDateTime
import com.kristianskokars.tasky.lib.randomID
import kotlinx.datetime.LocalDateTime

data class Event(
    val id: String = randomID(),
    val title: String = "New Meeting",
    val description: String = "New meeting description",
    val photos: List<Uri> = emptyList(),
    val fromDateTime: LocalDateTime = currentSystemDateTime(),
    val toDateTime: LocalDateTime = currentSystemDateTime(),
    val remindAtTime: RemindAtTime = RemindAtTime.ThirtyMinutesBefore,
    val creator: Attendee? = null,
    val attendees: List<Attendee> = emptyList(),
)
