package com.derspusulasi.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.derspusulasi.MainActivity
import com.derspusulasi.R
import com.derspusulasi.data.MotivationQuotes

object NotificationHelper {
    private const val CHANNEL_ID = "derspusulasi_channel"
    private const val CHANNEL_NAME = "DersPusulası Bildirimleri"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Pomodoro ve geri sayım bildirimleri"
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showPomodoroFinishedNotification(context: Context, isFocusMode: Boolean) {
        val message = if (isFocusMode) {
            "Mola zamanı! ${MotivationQuotes.getRandomQuote()}"
        } else {
            "Odak zamanı! ${MotivationQuotes.getRandomQuote()}"
        }

        showNotification(context, "Pomodoro", message)
    }

    fun showCountdownFinishedNotification(context: Context) {
        val message = "Geri sayım tamamlandı! ${MotivationQuotes.getRandomQuote()}"
        showNotification(context, "Geri Sayım", message)
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
