package com.kristianskokars.tasky.feature.event.presentation.event

import com.kristianskokars.tasky.core.domain.model.Event
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
import com.kristianskokars.tasky.feature.event.domain.model.AttendeeStatusFilter
import com.kristianskokars.tasky.lib.currentSystemDate
import kotlinx.datetime.LocalDate

data class EventState(
    val event: Event? = null,
    val currentDate: LocalDate = currentSystemDate(),
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val isCheckingIfAttendeeExists: Boolean = false,
    val isCurrentUserGoing: Boolean = false,
    val selectedStatusFilter: AttendeeStatusFilter = AttendeeStatusFilter.ALL,
    val goingAttendees: List<Attendee> = emptyList(),
    val notGoingAttendees: List<Attendee> = emptyList(),
) {
    val isHostEditing get() = isEditing && event?.isUserEventCreator == true
    val isCurrentUserHost get() = event?.isUserEventCreator == true
}
