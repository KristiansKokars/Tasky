package com.kristianskokars.tasky.feature.task.presentation

import com.kristianskokars.tasky.feature.task.domain.model.Task

data class TaskScreenState(
    val task: Task = Task(),
    val isEditing: Boolean = true,
)