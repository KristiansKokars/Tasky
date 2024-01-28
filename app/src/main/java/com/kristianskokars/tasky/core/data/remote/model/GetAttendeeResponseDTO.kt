package com.kristianskokars.tasky.core.data.remote.model
import kotlinx.serialization.Serializable

@Serializable
data class GetAttendeeResponseDTO(
    val doesUserExist: Boolean,
    val attendee: AttendeeDTO? = null,
)

@Serializable
data class AttendeeDTO(val email: String, val fullName: String, val userId: String)
