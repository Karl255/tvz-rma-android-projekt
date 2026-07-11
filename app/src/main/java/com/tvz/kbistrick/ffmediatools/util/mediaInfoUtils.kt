package com.tvz.kbistrick.ffmediatools.util

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import com.tvz.kbistrick.ffmediatools.model.MediaFormat
import com.tvz.kbistrick.ffmediatools.model.MediaInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun loadMediaInfo(context: Context, uri: Uri): MediaInfo = withContext(Dispatchers.IO) {
    val mimeType = context.contentResolver.getType(uri)
    val isVideo = mimeType?.startsWith("video/") == true

    val (width, height) =
        if (isVideo) loadVideoDimensions(context, uri)
        else loadImageDimensions(context, uri)

    MediaInfo(
        uri = uri,
        fileName = loadDisplayName(context, uri),
        mimeType = mimeType,
        width = width,
        height = height,
        isVideo = isVideo,
        format = MediaFormat.fromMimeType(mimeType, isVideo),
    )
}

private fun loadDisplayName(context: Context, uri: Uri): String {
    return context.contentResolver
        .query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        ?.use { cursor -> if (cursor.moveToFirst()) cursor.getString(0) else null }
        ?: error("Failed to read selected media's file name")

}

private fun loadImageDimensions(context: Context, uri: Uri): Pair<Int?, Int?> = try {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }

    context.contentResolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, options)
    }

    Pair(
        options.outWidth.takeIf { it > 0 },
        options.outHeight.takeIf { it > 0 },
    )
} catch (_: Exception) {
    Pair(null, null)
}

private fun loadVideoDimensions(context: Context, uri: Uri): Pair<Int?, Int?> {
    val retriever = MediaMetadataRetriever()

    return try {
        retriever.setDataSource(context, uri)

        val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            ?.toIntOrNull()
        val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            ?.toIntOrNull()
        val rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
            ?.toIntOrNull() ?: 0

        if (rotation == 90 || rotation == 270) Pair(height, width) else Pair(width, height)
    } catch (_: Exception) {
        Pair(null, null)
    } finally {
        retriever.release()
    }
}
