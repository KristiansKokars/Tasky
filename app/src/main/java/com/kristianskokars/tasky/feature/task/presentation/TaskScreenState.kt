package com.kristianskokars.tasky.feature.task.presentation

import com.kristianskokars.tasky.feature.task.domain.model.Task
import com.kristianskokars.tasky.lib.currentSystemDate
import kotlinx.datetime.LocalDate

data class TaskScreenState(
    val task: Task = Task(),
    val currentDate: LocalDate = currentSystemDate(),
    val isSaving: Boolean = false,
    val isEditing: Boolean = true,
)
