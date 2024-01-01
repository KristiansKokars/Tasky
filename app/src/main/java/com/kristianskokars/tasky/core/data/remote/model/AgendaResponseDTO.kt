package com.kristianskokars.tasky.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AgendaResponseDTO(
    val events: List<EventResponseDTO>,
    val tasks: List<TaskResponseDTO>,
    val reminders: List<ReminderResponseDTO>
)
