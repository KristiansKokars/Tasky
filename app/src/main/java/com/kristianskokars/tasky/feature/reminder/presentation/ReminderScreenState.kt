package com.kristianskokars.tasky.feature.reminder.presentation

import com.kristianskokars.tasky.feature.reminder.domain.model.Reminder
import com.kristianskokars.tasky.lib.allTimesOfDay
import com.kristianskokars.tasky.lib.currentSystemDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ReminderScreenState(
    val reminder: Reminder? = null,
    val currentDate: LocalDate = currentSystemDate(),
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val allowedTimeRange: ClosedRange<LocalTime> = allTimesOfDay(),
)
