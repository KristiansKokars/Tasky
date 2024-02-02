package com.kristianskokars.tasky.feature.event.presentation.event

import com.kristianskokars.tasky.core.domain.model.Event
import com.kristianskokars.tasky.lib.currentSystemDate
import kotlinx.datetime.LocalDate

data class EventState(
    val event: Event? = null,
    val currentDate: LocalDate = currentSystemDate(),
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val isCheckingIfAttendeeExists: Boolean = false,
    val errorAttendeeDoesNotExist: Boolean = false,
)
