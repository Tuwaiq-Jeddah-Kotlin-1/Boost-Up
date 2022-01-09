package com.mahila.motivationalQuotesApp.utils

import android.view.View
import androidx.work.*
import com.mahila.motivationalQuotesApp.worker.NotificationWorker
import java.util.*
import java.util.concurrent.TimeUnit

object ReminderUtil {
    fun setReminder(
        dateAndTime: Long, randomQuote: String,
        isEveryDay: Boolean, reminderId: String, view: View
    ) {
        val currentTime = Calendar.getInstance().timeInMillis
        val delay = dateAndTime - currentTime
        val data = Data.Builder().putInt(NotificationWorker.NOTIFICATION_ID, 0).build()
        val quoteNotification = Data.Builder().putString(
            NotificationWorker.NOTIFICATION_CONTENT_ID,
            randomQuote
        ).build()

        if (isEveryDay) {
            val periodicNotificationWork =
                PeriodicWorkRequest.Builder(
                    NotificationWorker::class.java,
                    24,
                    TimeUnit.HOURS
                ).addTag(reminderId)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data)
                    .setInputData(quoteNotification)
                    .build()
            val instanceWorkManager2 = WorkManager.getInstance(view.context)
            instanceWorkManager2.enqueueUniquePeriodicWork(
                NotificationWorker.NOTIFICATION_WORK,
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicNotificationWork
            )
        } else {
            val notificationWork =
                OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                    .addTag(reminderId)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data)
                    .setInputData(quoteNotification)
                    .build()

            val instanceWorkManager = WorkManager.getInstance(view.context)
            instanceWorkManager.beginUniqueWork(
                NotificationWorker.NOTIFICATION_WORK,
                ExistingWorkPolicy.REPLACE,
                notificationWork
            ).enqueue()
        }
    }

}