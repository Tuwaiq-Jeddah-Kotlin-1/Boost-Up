package com.mahila.motivationalQuotesApp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mahila.motivationalQuotesApp.views.homeScreen.MainActivity


class NotificationHelper(val context: Context) {
    private val CHANNEL_ID = "CHANNEL_ID"
    private val NOTIFICATION_ID = 1

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT
            ).apply {

                description = "channel des"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(title: String, msg: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        //  val icon = AppCompatResources.getDrawable(context, R.drawable.ic_bell)
        val notification = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.btn_star_big_on)
            .setContentTitle(title)
            .setContentText(msg)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

       NotificationManagerCompat.from(context).notify(NOTIFICATION_ID,notification)

        // .setTicker("").setAutoCancel(true) .setStyle(NotificationCompat.BigPictureStyle())
    }
} 

