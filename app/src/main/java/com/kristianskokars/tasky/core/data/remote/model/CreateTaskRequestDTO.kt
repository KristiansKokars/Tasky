package com.kristianskokars.tasky.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateTaskRequestDTO(
    val id: String,
    val title: String,
    val description: String? = null,
    val time: Long,
    val remindAt: Long,
    val isDone: Boolean
)