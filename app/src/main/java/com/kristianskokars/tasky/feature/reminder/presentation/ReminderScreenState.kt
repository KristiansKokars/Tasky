package com.kristianskokars.tasky.feature.reminder.presentation

import com.kristianskokars.tasky.feature.reminder.domain.model.Reminder

data class ReminderScreenState(
    val reminder: Reminder = Reminder(),
    val isEditing: Boolean = false,
)
