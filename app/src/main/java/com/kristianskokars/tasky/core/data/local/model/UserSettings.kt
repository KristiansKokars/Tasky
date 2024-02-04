package com.kristianskokars.tasky.core.data.local.model

import kotlinx.serialization.Serializable

// TODO: rework to AppSettings with separate user parameter
@Serializable
data class UserSettings(
    val userId: String? = null,
    val fullName: String? = null,
    val email: String? = null,
    val token: String? = null
)
