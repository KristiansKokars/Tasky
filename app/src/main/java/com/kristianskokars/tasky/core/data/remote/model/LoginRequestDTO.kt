package com.kristianskokars.tasky.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO(
    val email: String,
    val password: String
)
