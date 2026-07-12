package com.tvz.kbistrick.ffmediatools.service

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.icu.util.Calendar
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_DEFERRED
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.PermissionChecker
import com.mzgs.ffmpegx.FFmpeg
import com.mzgs.ffmpegx.MediaInformation
import com.tvz.kbistrick.ffmediatools.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FFMpegJobRunningService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        when (intent?.action) {
            Actions.START_WITH_ARGS.toString() -> {
                val commandArgs = intent.getStringExtra("commandArgs") ?: error("No command arguments provided")
                val notificationDescription = intent.getStringExtra("notificationDescription")
                val outputPath = intent.getStringExtra("outputPath") ?: error("No output path provided")

                start(startId, commandArgs, notificationDescription, outputPath)
            }

            Actions.ABORT.toString() -> {
                Log.i("FFMpegJobRunningService", "Received abort action")
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    private fun start(startId: Int, commandArgs: String, notificationDescription: String?, outputPath: String) {
        val notification = NotificationCompat.Builder(this, JOB_PROCESSING_NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.app_icon_dark)
            .setContentTitle("Media is processing")
            .setContentText(notificationDescription)
            .setOngoing(true)
            .setForegroundServiceBehavior(FOREGROUND_SERVICE_DEFERRED)
            .build()

        startForeground(
            JOB_PROCESSING_NOTIFICATION_ID,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROCESSING
        )

        val notificationPermission = PermissionChecker.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)

        if (notificationPermission != PermissionChecker.PERMISSION_GRANTED) {
            Log.w("FFMpegJobRunningService", "Notification permission not granted")
        }

        val time1 = Calendar.getInstance().time
        var success = true

        serviceScope.launch {
            try {
                Log.d("FFMpegJobRunningService", "Media processing starting")
                val isSuccessful = FFmpeg.getInstance().execute(commandArgs)
                Log.d("FFMpegJobRunningService", "Media processing ended")

                val outputMediaInfo = FFmpeg.getInstance().getMediaInfo(outputPath)
                if (outputMediaInfo == null) {
                    Log.e("FFMpegJobRunningService", "Getting processed media info failed")
                } else {
                    Log.d("FFMpegJobRunningService", "Getting processed media info success")
                }

                val thumbnailPath = getThumbnail(outputPath)
                if (thumbnailPath == null) {
                    Log.e("FFMpegJobRunningService", "Creading thumbnail failed")
                } else {
                    Log.d("FFMpegJobRunningService", "Creading thumbnail success")
                }

                if (isSuccessful) {
                    Log.d("FFMpegJobRunningService", "Media processing success")

                    sendJobFinishedBroadcast(outputPath, outputMediaInfo, thumbnailPath)
                } else {
                    success = false
                    Log.e("FFMpegJobRunningService", "FFmpeg execution failed")
                }
            } catch (e: Exception) {
                success = false
                Log.e("FFMpegJobRunningService", e.message, e)
            } finally {
                val time2 = Calendar.getInstance().time
                val duration = time2.time - time1.time
                showFinishedNotification(success, duration)
                stopSelf(startId)
            }
        }
    }

    fun showFinishedNotification(successful: Boolean, durationMs: Long) {
        val notification = NotificationCompat.Builder(this, JOB_FINISHED_NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.app_icon_dark)
            .setContentTitle(if (successful) "Media processing finished" else "Media processing failed")
            .setAutoCancel(true)
            .setSilent(durationMs > 5000 || !successful)
            .apply {
                if (durationMs <= 5000) {
                    setTimeoutAfter(5000)
                }
            }
            .build()

        with(NotificationManagerCompat.from(this)) {
            cancel(JOB_PROCESSING_NOTIFICATION_ID)

            val notificationPermission = PermissionChecker.checkSelfPermission(this@FFMpegJobRunningService, Manifest.permission.POST_NOTIFICATIONS)

            if (notificationPermission != PermissionChecker.PERMISSION_GRANTED) {
                Log.w("FFMpegJobRunningService", "Notification permission not granted")
            }

            notify(JOB_FINISHED_NOTIFICATION_ID, notification)
        }
    }

    fun sendJobFinishedBroadcast(outputPath: String, outputMediaInfo: MediaInformation?, thumbnailPath: String?) {
        sendBroadcast(
            Intent(ACTION_JOB_FINISHED).apply {
                setPackage(packageName)
                putExtra(ACTION_JOB_FINISHED_OUTPUT_PATH, outputPath)
                outputMediaInfo?.videoStreams[0]?.width?.let { putExtra(ACTION_JOB_FINISHED_WIDTH, it) }
                outputMediaInfo?.videoStreams[0]?.height?.let { putExtra(ACTION_JOB_FINISHED_HEIGHT, it) }
                thumbnailPath?.let { putExtra(ACTION_JOB_FINISHED_THUMBNAIL_PATH, it) }
            }
        )
    }

    private suspend fun getThumbnail(mediaPath: String): String? {
        val thumbnailPath = "${mediaPath}_THUMB.jpg"

        val success = FFmpeg.getInstance().execute("""-i $mediaPath -frames:v 1 -q:v 3 $thumbnailPath""")

        return if (success) {
            thumbnailPath
        } else {
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    enum class Actions {
        START_WITH_ARGS, ABORT
    }

    companion object {
        const val JOB_PROCESSING_NOTIFICATION_ID = 1
        const val JOB_PROCESSING_NOTIFICATION_CHANNEL = "job_progress"
        const val JOB_FINISHED_NOTIFICATION_ID = 2
        const val JOB_FINISHED_NOTIFICATION_CHANNEL = "job_finished"

        const val ACTION_JOB_FINISHED = "com.tvz.kbistrick.ffmediatools.ACTION_JOB_FINISHED"
        const val ACTION_JOB_FINISHED_OUTPUT_PATH = "outputPath"
        const val ACTION_JOB_FINISHED_WIDTH = "width"
        const val ACTION_JOB_FINISHED_HEIGHT = "height"
        const val ACTION_JOB_FINISHED_THUMBNAIL_PATH = "thumbnailPath"
    }
}
