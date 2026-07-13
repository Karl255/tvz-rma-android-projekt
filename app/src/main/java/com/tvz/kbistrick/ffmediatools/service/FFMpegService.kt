package com.tvz.kbistrick.ffmediatools.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.tvz.kbistrick.ffmediatools.model.Action
import com.tvz.kbistrick.ffmediatools.model.DimensionUnit
import com.tvz.kbistrick.ffmediatools.model.DimensionValue
import com.tvz.kbistrick.ffmediatools.model.MediaFormat
import com.tvz.kbistrick.ffmediatools.model.MediaInfo
import com.tvz.kbistrick.ffmediatools.model.VideoCodec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

// TODO rename to media processing service
object FFMpegService {
    private const val TAG = "FFMpegService"

    suspend fun runScalingJob(
        context: Context,
        mediaInfo: MediaInfo,
        width: DimensionValue,
        height: DimensionValue
    ) {
        Log.i(TAG, "Scaling job initiated")
        val fileName = mediaInfo.fileName
        val input = copyToCache(context, mediaInfo.uri, fileName)
        val output = File(context.cacheDir, input.name.addFilenameSuffix("_s"))

        val widthParam = width.toRelativeParam("iw")
        val heightParam = height.toRelativeParam("ih")
        val commandArgs = listOf(
            "-y",
            "-hide_banner",
            "-i",
            input.absolutePath,
            "-vf",
            "scale=$widthParam:$heightParam",
            "-b:v",
            "8M",
            output.absolutePath
        )

        val intent = Intent(context.applicationContext, FFMpegJobRunningService::class.java).also {
            it.action = Action.StartJob.ACTION
            it.putExtra(Action.StartJob.ARGS, commandArgs.toTypedArray())
            it.putExtra(Action.StartJob.OUTPUT_PATH, output.absolutePath)
            it.putExtra(Action.StartJob.NOTIFICATION_DESCRIPTION, "Scaling ${if (mediaInfo.isVideo) "video" else "image"}")
        }

        context.startForegroundService(intent)
    }

    suspend fun runCropVideoJob(
        context: Context,
        mediaInfo: MediaInfo,
        top: DimensionValue,
        bottom: DimensionValue,
        left: DimensionValue,
        right: DimensionValue,
    ) {
        Log.i(TAG, "Crop video job initiated")
        val fileName = mediaInfo.fileName
        val input = copyToCache(context, mediaInfo.uri, fileName)
        val output = File(context.cacheDir, input.name.addFilenameSuffix("_s"))

        val x = left.toRelativeParam("in_w")
        val width = "in_w-${left.toRelativeParam("in_w")}-${right.toRelativeParam("in_w")}"
        val y = top.toRelativeParam("in_w")
        val height = "in_h-${top.toRelativeParam("in_h")}-${bottom.toRelativeParam("in_h")}"

        val commandArgs = listOf(
            "-y",
            "-hide_banner",
            "-i",
            input.absolutePath,
            "-vf",
            "crop=$width:$height:$x:$y",
            "-b:v",
            "8M",
            output.absolutePath
        )

        val intent = Intent(context.applicationContext, FFMpegJobRunningService::class.java).also {
            it.action = Action.StartJob.ACTION
            it.putExtra(Action.StartJob.ARGS, commandArgs.toTypedArray())
            it.putExtra(Action.StartJob.OUTPUT_PATH, output.absolutePath)
            it.putExtra(Action.StartJob.NOTIFICATION_DESCRIPTION, "Scaling ${if (mediaInfo.isVideo) "video" else "image"}")
        }

        context.startForegroundService(intent)
    }

    suspend fun runConversionJob(
        context: Context,
        mediaInfo: MediaInfo,
        outputFormat: MediaFormat,
        videoCodec: VideoCodec?,
        options: List<String>,
    ) {
        Log.i(TAG, "Conversion job initiated")
        val fileName = mediaInfo.fileName
        val input = copyToCache(context, mediaInfo.uri, fileName)
        val output = File(context.cacheDir, input.name.changeExtension("_c", outputFormat.fileExtension))

        val commandArgs = listOf(
            "-y",
            "-hide_banner",
            "-i",
            input.absolutePath,
            *options.toTypedArray(),
            output.absolutePath
        )

        val intent = Intent(context.applicationContext, FFMpegJobRunningService::class.java).also {
            it.action = Action.StartJob.ACTION
            it.putExtra(Action.StartJob.ARGS, commandArgs.toTypedArray())
            it.putExtra(Action.StartJob.OUTPUT_PATH, output.absolutePath)
            it.putExtra(Action.StartJob.NOTIFICATION_DESCRIPTION, "Scaling ${if (mediaInfo.isVideo) "video" else "image"}")
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

    private fun DimensionValue.toRelativeParam(coefficientForPercentage: String) = when (unit) {
        DimensionUnit.PIXEL -> "$number"
        DimensionUnit.PERCENT -> "${number / 100f}*$coefficientForPercentage"
    }

    private fun String.addFilenameSuffix(suffix: String): String {
        val dotIndex = this.lastIndexOf('.')
        return if (dotIndex == -1) {
            "$this$suffix"
        } else {
            this.substring(0, dotIndex) + suffix + this.substring(dotIndex)
        }
    }

    private fun String.changeExtension(suffix: String, newExtension: String): String {
        val dotIndex = this.lastIndexOf('.')
        val name = if (dotIndex == -1) this else this.substring(0, dotIndex)

        return "$name$suffix.$newExtension"
    }
}
