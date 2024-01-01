package com.kristianskokars.tasky.core.data.remote.model
import kotlinx.serialization.Serializable

@Serializable
data class EventResponseDTO(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: AttendeeResponseDTO,
    val photos: PhotoResponseDTO
)
