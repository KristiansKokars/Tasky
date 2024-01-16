package com.kristianskokars.tasky.feature.task.presentation

data class TaskScreenState(
    val task: Task = Task(),
    val isEditing: Boolean = true,
)