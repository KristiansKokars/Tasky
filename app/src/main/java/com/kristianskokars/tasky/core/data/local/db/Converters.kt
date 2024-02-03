package com.kristianskokars.tasky.core.data.local.db

import androidx.room.TypeConverter
import com.kristianskokars.tasky.core.domain.model.Photo
import com.kristianskokars.tasky.lib.json
import kotlinx.serialization.encodeToString

class Converters {
    @TypeConverter
    fun photosFromString(value: String?): List<Photo>? {
        return value?.let { json.decodeFromString<List<Photo>>(it) }
    }

    @TypeConverter
    fun photosToString(photos: List<Photo>?): String? {
        return photos?.let { json.encodeToString(it) }
    }
}
