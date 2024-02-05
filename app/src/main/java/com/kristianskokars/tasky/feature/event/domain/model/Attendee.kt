package com.kristianskokars.tasky.feature.event.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Attendee(
    val userId: String,
    val email: String,
    val fullName: String,
    val eventId: String,
    val isGoing: Boolean,
)
