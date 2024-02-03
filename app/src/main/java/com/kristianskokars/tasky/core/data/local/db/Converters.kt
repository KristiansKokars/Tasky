package com.kristianskokars.tasky.core.data.local.db

import androidx.room.TypeConverter
import com.kristianskokars.tasky.core.domain.model.Photo
import com.kristianskokars.tasky.feature.event.domain.model.Attendee
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

    @TypeConverter
    fun attendeesFromString(value: String?): List<Attendee>? {
        return value?.let { json.decodeFromString<List<Attendee>>(it) }
    }

    @TypeConverter
    fun attendeesToString(attendees: List<Attendee>?): String? {
        return attendees?.let { json.encodeToString(it) }
    }
}
