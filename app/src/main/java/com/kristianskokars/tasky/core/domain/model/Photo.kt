package com.kristianskokars.tasky.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Photo(
    val key: String,
    val url: String,
    val isLocal: Boolean = false,
): Parcelable
