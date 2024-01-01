package com.kristianskokars.tasky.core.data.remote.model
import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponseDTO(
    val key: String,
    val url: String
)
