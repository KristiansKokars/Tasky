package com.kristianskokars.tasky.feature.event.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.kristianskokars.tasky.feature.event.domain.PhotoConverter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class AndroidPhotoConverter(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : PhotoConverter {
    override suspend fun compressPhoto(uri: Uri): Uri? {
        return withContext(ioDispatcher) {
            val cursor = context.contentResolver.query(uri, null, null, null, null)!!
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            val size = cursor.getLong(sizeIndex)

            Timber.d("Bytes of pic: $size")
            cursor.close()

            val photoBytes = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val outputStream = ByteArrayOutputStream()

                if (size > 1_000_000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }

                outputStream.toByteArray()
            }

            val picture = File(context.cacheDir.toString(), "photo-${Random.nextInt()}")
            picture.createNewFile()
            FileOutputStream(picture).use { fos ->
                fos.write(photoBytes)
                fos.flush()
            }

            Timber.d("Photo bytes: ${photoBytes?.size}")
            if (photoBytes != null && photoBytes.size < 1_000_000) {
                return@withContext picture.toUri()
            } else {
                Toast
                    .makeText(context, "Could not upload file due to it being too large", Toast.LENGTH_SHORT)
                    .show()
                return@withContext null
            }
        }
    }

    override fun photosToMultipart(photos: List<Uri>): List<MultipartBody.Part> {
        val parts = mutableListOf<MultipartBody.Part>()
        for ((i, item) in photos.withIndex()) {
            parts.add(prepareFilePart(context, "photo[$i]", item))
        }
        return parts
    }

    private fun prepareFilePart(context: Context, partName: String, fileUri: Uri): MultipartBody.Part{
        val file = fileUri.toFile()
        val requestBody = file.asRequestBody(context.contentResolver.getType(fileUri)!!.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }
}
