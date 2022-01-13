package com.mahila.motivationalQuotesApp.utils

import androidx.work.*
import com.mahila.motivationalQuotesApp.app.BoostUp
import com.mahila.motivationalQuotesApp.worker.NotificationWorker
import java.util.*
import java.util.concurrent.TimeUnit

object ReminderUtil {
    fun setReminder(
        dateAndTime: Long, randomQuote: String,
        isEveryDay: Boolean, reminderId: String
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
            val instanceWorkManager2 = WorkManager.getInstance(BoostUp.instant.applicationContext)
            instanceWorkManager2.enqueueUniquePeriodicWork(
                reminderId,
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

            val instanceWorkManager = WorkManager.getInstance(BoostUp.instant.applicationContext)

            instanceWorkManager.beginUniqueWork(
                reminderId,
                ExistingWorkPolicy.REPLACE,
                notificationWork
            ).enqueue()
        }
    }

}