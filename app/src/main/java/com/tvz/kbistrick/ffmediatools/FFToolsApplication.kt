package com.tvz.kbistrick.ffmediatools

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class FFToolsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager

        notificationManager.createNotificationChannel(
            NotificationChannel(
                "job_progress",
                "Media is progressing",
                NotificationManager.IMPORTANCE_MIN
            )
        )

        notificationManager.createNotificationChannel(
            NotificationChannel(
                "job_finished",
                "Media progressing finished",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
    }
}