package com.tvz.kbistrick.ffmediatools.service

import android.content.Context
import android.net.Uri
import com.mzgs.ffmpegx.FFmpeg
import com.tvz.kbistrick.ffmediatools.model.DimensionValue
import com.tvz.kbistrick.ffmediatools.model.MediaInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object FFMpegService {
    suspend fun runScalingJob(
        context: Context,
        ffmpeg: FFmpeg,
        mediaInfo: MediaInfo,
        width: DimensionValue,
        height: DimensionValue
    ) {
        val input = copyToCache(context, mediaInfo.uri, mediaInfo.fileName)
        val output = File(context.cacheDir, "scaled-${input.name}")

        val widthPixels = width.toPixels(mediaInfo.width)
        val heightPixels = height.toPixels(mediaInfo.height)

        ffmpeg.execute("-y -i \"${input.absolutePath}\" -vf scale=$widthPixels:$heightPixels \"${output.absolutePath}\"")
    }

    private suspend fun copyToCache(context: Context, uri: Uri, fileName: String?): File =
        withContext(Dispatchers.IO) {
            val file = File(context.cacheDir, fileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            } ?: error("Could not open input stream for $uri")

            file
        }
}