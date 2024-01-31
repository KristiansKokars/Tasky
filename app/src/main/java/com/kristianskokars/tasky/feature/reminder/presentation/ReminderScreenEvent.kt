package com.kristianskokars.tasky.feature.reminder.presentation

import com.kristianskokars.tasky.core.data.local.model.RemindAtTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed class ReminderScreenEvent {
    data object Save : ReminderScreenEvent()
    data object Delete : ReminderScreenEvent()
    data object BeginEditing : ReminderScreenEvent()
    data class OnUpdateTime(val newTime: LocalTime) : ReminderScreenEvent()
    data class OnUpdateDate(val newDate: LocalDate) : ReminderScreenEvent()
    data class OnChangeRemindAtTime(val newRemindAtTime: RemindAtTime) : ReminderScreenEvent()
    data class OnTitleChanged(val newTitle: String) : ReminderScreenEvent()
    data class OnDescriptionChanged(val newDescription: String) : ReminderScreenEvent()
}