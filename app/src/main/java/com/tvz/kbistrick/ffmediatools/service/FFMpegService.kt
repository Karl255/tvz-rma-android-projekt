package com.tvz.kbistrick.ffmediatools.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.tvz.kbistrick.ffmediatools.model.DimensionUnit
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
        Log.i("FFMpegService", "Scaling job initiated")
        val fileName = mediaInfo.fileName
        val input = copyToCache(context, mediaInfo.uri, fileName)
        val output = File(context.cacheDir, input.name.addFilenameSuffix("_s"))

        val widthParam = width.toScalingParam("iw")
        val heightParam = height.toScalingParam("ih")
        val commandArgs =
            """-y -i "${input.absolutePath}" -vf "scale=$widthParam:$heightParam" "${output.absolutePath}""""

        val intent = Intent(context.applicationContext, FFMpegJobRunningService::class.java).also {
            it.action = FFMpegJobRunningService.Actions.START_WITH_ARGS.toString()
            it.putExtra("commandArgs", commandArgs)
            it.putExtra("inputFilename", input.name)
            it.putExtra("outputPath", output.absolutePath)
            it.putExtra("notificationDescription", "Scaling ${if (mediaInfo.isVideo) "ideo" else "image"}")
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

    private fun DimensionValue.toScalingParam(coefficientForPercentage: String) = when (unit) {
        DimensionUnit.PIXEL -> "$number"
        DimensionUnit.PERCENT -> "$coefficientForPercentage*${number/100f}"
    }

    private fun String.addFilenameSuffix(suffix: String): String {
        val dotIndex = this.lastIndexOf('.')
        return if (dotIndex == -1) {
            "$this$suffix"
        } else {
            this.substring(0, dotIndex) + suffix + this.substring(dotIndex)
        }
    }
}
