package com.tvz.kbistrick.ffmediatools.service

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.PermissionChecker
import com.mzgs.ffmpegx.FFmpeg
import com.tvz.kbistrick.ffmediatools.FatalAppException
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
                val commandArgs = intent.getStringExtra("commandArgs")
                    ?: throw FatalAppException("No command arguments provided")

                start(commandArgs, startId)
            }

            Actions.ABORT.toString() -> stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun start(commandArgs: String, startId: Int) {
        val notificationPermission =
            PermissionChecker.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)

        if (notificationPermission != PermissionChecker.PERMISSION_GRANTED) {
            stopSelf(startId)
            return
        }

        val notification = NotificationCompat.Builder(this, "job_progress")
            .setSmallIcon(R.drawable.app_icon_dark)
            .setContentText("Media conversion progress")
            .setContentText("placehlder")
            .setOngoing(true)
            .build()

        ServiceCompat.startForeground(
            this,
            1,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROCESSING
        )

        serviceScope.launch {
            try {
                FFmpeg.getInstance().executeAsync(commandArgs)
            } catch (e: Exception) {
                Log.e("FFMpegJobRunningService", e.message, e)
            } finally {
                stopSelf(startId)
            }
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
