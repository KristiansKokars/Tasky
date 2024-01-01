package com.kristianskokars.tasky.core.data.remote.model
import kotlinx.serialization.Serializable

@Serializable
data class AttendeeResponseDTO(
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long
)
