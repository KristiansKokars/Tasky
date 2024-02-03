package com.kristianskokars.tasky.feature.event.domain

import android.net.Uri
import com.kristianskokars.tasky.core.domain.model.Photo
import okhttp3.MultipartBody


interface PhotoConverter {
    suspend fun compressPhoto(uri: Uri): Uri?
    fun photosToMultipart(photos: List<Photo>): List<MultipartBody.Part>
}
