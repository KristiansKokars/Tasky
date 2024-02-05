package com.kristianskokars.tasky.feature.event.presentation.event

import android.net.Uri
import com.kristianskokars.tasky.core.domain.model.Photo
import com.kristianskokars.tasky.core.domain.model.RemindAtTime
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
import com.kristianskokars.tasky.feature.event.domain.model.AttendeeStatusFilter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed class EventScreenEvent {
    data class OnAddPhoto(val newPhoto: Uri) : EventScreenEvent()
    data class OnRemovePhoto(val photoToRemove: Photo) : EventScreenEvent()
    data object BeginEditing : EventScreenEvent()
    data object Save : EventScreenEvent()
    data object Delete : EventScreenEvent()
    data class AddAttendee(val addAttendeeEmail: String) : EventScreenEvent()
    data class RemoveAttendee(val attendeeToRemove: Attendee) : EventScreenEvent()
    data class OnUpdateFromTime(val newFromTime: LocalTime) : EventScreenEvent()
    data class OnUpdateToTime(val newToTime: LocalTime) : EventScreenEvent()
    data class OnUpdateFromDate(val newFromDate: LocalDate) : EventScreenEvent()
    data class OnUpdateToDate(val newToDate: LocalDate) : EventScreenEvent()
    data class OnChangeRemindAtTime(val newRemindAtTime: RemindAtTime) : EventScreenEvent()
    data class OnTitleChanged(val newTitle: String) : EventScreenEvent()
    data class OnDescriptionChanged(val newDescription: String) : EventScreenEvent()
    data class SwitchStatusFilter(val newFilter: AttendeeStatusFilter) : EventScreenEvent()
}
