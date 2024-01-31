package com.kristianskokars.tasky.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateReminderRequestDTO(
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
)
