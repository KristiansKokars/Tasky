package com.kristianskokars.tasky.feature.event.domain

import android.net.Uri
import okhttp3.MultipartBody


interface PhotoConverter {
    suspend fun compressPhoto(uri: Uri): Uri?
    fun photosToMultipart(photos: List<Uri>): List<MultipartBody.Part>
}
