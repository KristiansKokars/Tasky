package com.kristianskokars.tasky.feature.event.presentation

import com.kristianskokars.tasky.lib.currentSystemDate
import kotlinx.datetime.LocalDate

data class EventState(
    val id: String? = null,
    val title: String = "Meeting",
    val description: String? = null,
    val isEditing: Boolean = false,
    val currentDate: LocalDate = currentSystemDate()
)