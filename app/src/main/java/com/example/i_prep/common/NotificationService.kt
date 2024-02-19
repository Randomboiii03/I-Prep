package com.example.i_prep.common

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.i_prep.R
import kotlin.random.Random

class NotificationService(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun showNotification(message: String, isError: Boolean) {
        val notification = NotificationCompat.Builder(context, "i_prep_reminder")
            .setContentTitle("I-Prep")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setSmallIcon(if (isError) R.drawable.baseline_error_24 else R.drawable.baseline_notification_important_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }
}