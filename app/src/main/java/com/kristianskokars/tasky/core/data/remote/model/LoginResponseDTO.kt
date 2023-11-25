package com.kristianskokars.tasky.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDTO(
    val token: String,
    val userId: String,
    val fullName: String
)
