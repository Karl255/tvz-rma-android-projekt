package com.tvz.kbistrick.ffmediatools.util

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import java.io.File

fun saveToGallery(context: Context, filePath: String) {
    val file = File(filePath)
    if (!file.exists()) {
        Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show()
        return
    }

    val extension = file.extension
    val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
    val isVideo = mimeType?.startsWith("video/") == true || listOf("mp4", "mkv", "webm").contains(extension.lowercase())

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        val relativePath = if (isVideo) Environment.DIRECTORY_MOVIES else Environment.DIRECTORY_PICTURES
        put(MediaStore.MediaColumns.RELATIVE_PATH, "$relativePath/FFMediaTools")
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }

    val collection = if (isVideo) {
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val uri = context.contentResolver.insert(collection, contentValues)

    if (uri != null) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                file.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            context.contentResolver.update(uri, contentValues, null, null)
            Toast.makeText(context, "Saved to gallery", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            context.contentResolver.delete(uri, null, null)
            Toast.makeText(context, "Failed to save: ${e.message}", Toast.LENGTH_LONG).show()
        }
    } else {
        Toast.makeText(context, "Failed to create MediaStore entry", Toast.LENGTH_LONG).show()
    }
}
