package com.kristianskokars.tasky.feature.event.presentation.event

import android.net.Uri
import com.kristianskokars.tasky.core.domain.model.RemindAtTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed class EventScreenEvent {
    data class OnAddPhoto(val newPhoto: Uri) : EventScreenEvent()
    data object BeginEditing : EventScreenEvent()
    data object SaveEdits : EventScreenEvent()
    data object DeleteEvent : EventScreenEvent()
    data class AddAttendee(val addAttendeeEmail: String) : EventScreenEvent()
    data class OnUpdateFromTime(val newFromTime: LocalTime) : EventScreenEvent()
    data class OnUpdateToTime(val newToTime: LocalTime) : EventScreenEvent()
    data class OnUpdateFromDate(val newFromDate: LocalDate) : EventScreenEvent()
    data class OnUpdateToDate(val newToDate: LocalDate) : EventScreenEvent()
    data class OnChangeRemindAtTime(val newRemindAtTime: RemindAtTime) : EventScreenEvent()
    data class OnTitleChanged(val newTitle: String) : EventScreenEvent()
    data class OnDescriptionChanged(val newDescription: String) : EventScreenEvent()
}
