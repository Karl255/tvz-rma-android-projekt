package com.tvz.kbistrick.ffmediatools

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class FFToolsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            "job_progress", "Media conversion progress",
            NotificationManager.IMPORTANCE_MIN
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}