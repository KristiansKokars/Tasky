package com.kristianskokars.tasky.feature.reminder.presentation

import com.kristianskokars.tasky.feature.reminder.domain.model.Reminder
import com.kristianskokars.tasky.lib.allTimesOfDay
import kotlinx.datetime.LocalTime

data class ReminderScreenState(
    val reminder: Reminder = Reminder(),
    val isEditing: Boolean = false,
    val allowedTimeRange: ClosedRange<LocalTime> = allTimesOfDay(),
)
