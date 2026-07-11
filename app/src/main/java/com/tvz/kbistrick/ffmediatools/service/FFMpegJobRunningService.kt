package com.tvz.kbistrick.ffmediatools.service

import android.Manifest
import android.app.NotificationManager
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
import com.tvz.kbistrick.ffmediatools.R
import kotlinx.coroutines.*

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

                start(startId, commandArgs, notificationDescription)
            }

            Actions.ABORT.toString() -> {
                Log.i("FFMpegJobRunningService", "Received abort action")
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    private fun start(startId: Int, commandArgs: String, notificationDescription: String?) {
        val notification = NotificationCompat.Builder(this, "job_progress")
            .setSmallIcon(R.drawable.app_icon_dark)
            .setContentTitle("Media is processing")
            .setContentText(notificationDescription)
            .setOngoing(true)
            .setForegroundServiceBehavior(FOREGROUND_SERVICE_DEFERRED)
            .build()

        startForeground(
            1,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROCESSING
        )

        val notificationPermission = PermissionChecker.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)

        if (notificationPermission != PermissionChecker.PERMISSION_GRANTED) {
            Log.w("FFMpegJobRunningService", "Notification permission not granted")
            return
        }

        val time1 = Calendar.getInstance().time
        var success = true

        serviceScope.launch {
            try {
                FFmpeg.getInstance().executeAsync(commandArgs)
                Log.i("FFMpegJobRunningService", "Media processing finished")
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
        val notification = NotificationCompat.Builder(this, "job_finished")
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
            cancel(1)

            val notificationPermission = PermissionChecker.checkSelfPermission(this@FFMpegJobRunningService, Manifest.permission.POST_NOTIFICATIONS)

            if (notificationPermission != PermissionChecker.PERMISSION_GRANTED) {
                Log.w("FFMpegJobRunningService", "Notification permission not granted")
                return
            }

            notify(2, notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    enum class Actions {
        START_WITH_ARGS, ABORT
    }
}
