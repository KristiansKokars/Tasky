package com.kristianskokars.tasky.feature.task.presentation

import com.kristianskokars.tasky.core.domain.model.Task
import com.kristianskokars.tasky.lib.currentSystemDate
import kotlinx.datetime.LocalDate

data class TaskScreenState(
    val task: Task? = null,
    val currentDate: LocalDate = currentSystemDate(),
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,
)
