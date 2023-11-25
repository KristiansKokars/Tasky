package com.kristianskokars.tasky.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDTO(
    val fullName: String,
    val email: String,
    val password: String
)
