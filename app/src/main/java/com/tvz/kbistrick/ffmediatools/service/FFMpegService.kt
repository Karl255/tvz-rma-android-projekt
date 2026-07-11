package com.tvz.kbistrick.ffmediatools.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mzgs.ffmpegx.FFmpeg
import com.tvz.kbistrick.ffmediatools.MediaInfoReadException
import com.tvz.kbistrick.ffmediatools.model.DimensionValue
import com.tvz.kbistrick.ffmediatools.model.MediaInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object FFMpegService {
    suspend fun runScalingJob(
        context: Context,
        mediaInfo: MediaInfo,
        width: DimensionValue,
        height: DimensionValue
    ) {
        val fileName = mediaInfo.fileName!!
        val input = copyToCache(context, mediaInfo.uri, fileName)
        val output = File(context.cacheDir, "${input.name}-scaled")

        val inputMedia = FFmpeg.getInstance().getMediaInfo(input.absolutePath) ?: throw MediaInfoReadException()

        val widthPixels = width.toPixels(inputMedia.videoStreams[0].width)
        val heightPixels = height.toPixels(inputMedia.videoStreams[0].height)
        val commandArgs = """-y -i "${input.absolutePath}" -vf scale=$widthPixels:$heightPixels "${output.absolutePath}""""

        val intent = Intent(context.applicationContext, FFMpegJobRunningService::class.java).also {
            it.action = FFMpegJobRunningService.Actions.START_WITH_ARGS.toString()
            it.putExtra("commandArgs", commandArgs)
        }

        context.startForegroundService(intent)
    }

    private suspend fun copyToCache(context: Context, uri: Uri, fileName: String): File {
        return withContext(Dispatchers.IO) {
            val file = File(context.cacheDir, fileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            } ?: error("Could not open input stream for $uri")

            file
        }
    }
}