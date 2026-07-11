package com.tvz.kbistrick.ffmediatools

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.tvz.kbistrick.ffmediatools.service.FFMpegJobRunningService.Companion.JOB_FINISHED_NOTIFICATION_CHANNEL
import com.tvz.kbistrick.ffmediatools.service.FFMpegJobRunningService.Companion.JOB_PROCESSING_NOTIFICATION_CHANNEL

class FFToolsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        clearAppCache()

        val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager

        notificationManager.createNotificationChannel(
            NotificationChannel(
                JOB_PROCESSING_NOTIFICATION_CHANNEL,
                "Media is progressing",
                NotificationManager.IMPORTANCE_MIN
            )
        )

        notificationManager.createNotificationChannel(
            NotificationChannel(
                JOB_FINISHED_NOTIFICATION_CHANNEL,
                "Media progressing finished",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
    }

    private fun clearAppCache() {
        try {
            cacheDir.listFiles()?.forEach { it.deleteRecursively() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}