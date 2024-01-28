package com.kristianskokars.tasky.feature.event.presentation

import android.net.Uri
import com.kristianskokars.tasky.core.data.local.model.RemindAtTime
import com.kristianskokars.tasky.lib.currentSystemDate
import com.kristianskokars.tasky.lib.currentSystemDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class EventState(
    val id: String? = null,
    val title: String = "Meeting",
    val description: String? = null,
    val isEditing: Boolean = false,
    val photos: List<Uri> = emptyList(),
    val fromDateTime: LocalDateTime = currentSystemDateTime(),
    val toDateTime: LocalDateTime = currentSystemDateTime(),
    val remindAtTime: RemindAtTime = RemindAtTime.ThirtyMinutesBefore,
    val currentDate: LocalDate = currentSystemDate()
)
