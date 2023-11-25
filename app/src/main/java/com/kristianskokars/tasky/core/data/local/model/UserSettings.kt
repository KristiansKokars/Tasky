package com.kristianskokars.tasky.core.data.local.model

import kotlinx.serialization.Serializable

// TODO: can we null check this better?
@Serializable
data class UserSettings(
    val userId: String? = null,
    val fullName: String? = null,
    val token: String? = null
)
