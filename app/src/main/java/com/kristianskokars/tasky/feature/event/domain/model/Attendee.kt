package com.kristianskokars.tasky.feature.event.domain.model

data class Attendee(
    val userId: String,
    val email: String,
    val fullName: String
)