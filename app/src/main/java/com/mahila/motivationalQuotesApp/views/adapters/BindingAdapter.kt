package com.mahila.motivationalQuotesApp.views.adapters

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation.findNavController
import androidx.work.*
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.model.entities.Notification
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.repository.FirebaseUserService
import com.mahila.motivationalQuotesApp.worker.NotificationWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class BindingAdapters {
    companion object {

        @BindingAdapter("android:sendAQuoteToFavoritesList")
        @JvmStatic
        fun sendAQuoteToFavoritesList(view: ImageView, currentQuote: Quote) {
            view.setOnClickListener {
                if (view.tag != "LIKE") {
                    view.tag = "LIKE"
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_like
                        )
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        FirebaseUserService.addFavoriteQuote(currentQuote)
                    }
                } else {

                    view.tag = "UNLIKE"
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_unlike
                        )
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        FirebaseUserService.deleteFavoriteQuote(currentQuote)

                    }


                }

            }
        }

        @BindingAdapter("android:shareAQuoteViaOtherApps")
        @JvmStatic
        fun shareAQuoteViaOtherApps(view: ImageView, currentQuote: Quote) {
            view.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${currentQuote.text} \n\n${currentQuote.author}")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, "Share this quote with: ")
                startActivity(view.context, shareIntent, null)

            }
        }

        @BindingAdapter("android:inactiveNotificationIcon")
        @JvmStatic
        fun inactiveNotificationIcon(view: ImageView, currentNotification: Notification) {
            if (!currentNotification.active) {
                view.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_bell_gray
                    )
                )
            }
            view.setOnClickListener {
                println(currentNotification.active)
                println(view.tag.toString())
                if ( view.tag == "ACTIVE") {
                    view.tag = "INACTIVE"
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_bell_gray
                        )
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        //update the activity field
                        FirebaseUserService.setToInactiveNotification(currentNotification)
                    }
                    WorkManager.getInstance(view.context)
                        .cancelAllWorkByTag(currentNotification.notificationId)
                } else if (view.tag == "INACTIVE" ) {
                    view.tag = "ACTIVE"
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_bell_purple
                        )
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        FirebaseUserService.setToInactiveNotification(currentNotification)

                    }
                    val currentTime = Calendar.getInstance().timeInMillis
                    val delay = currentNotification.dateAndTime - currentTime
                    val data = Data.Builder().putInt(NotificationWorker.NOTIFICATION_ID, 0).build()
                    val quoteNotification = Data.Builder().putString(
                        NotificationWorker.NOTIFICATION_CONTENT_ID,
                        currentNotification.randomQuote
                    ).build()

                    if (currentNotification.everyDay) {
                        val periodicNotificationWork =
                            PeriodicWorkRequest.Builder(
                                NotificationWorker::class.java,
                                24,
                                TimeUnit.HOURS
                            )
                                .addTag(currentNotification.notificationId)
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
                                .addTag(currentNotification.notificationId)
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

        }

        @BindingAdapter("android:delNotificationIcon")
        @JvmStatic
        fun delNotificationIcon(view: View, currentNotification: Notification) {
            val currentTime = Calendar.getInstance().timeInMillis

            if ((!currentNotification.everyDay) && currentNotification.dateAndTime < currentTime) {

                view.visibility = View.GONE
                GlobalScope.launch(Dispatchers.IO) {
                    FirebaseUserService.deleteNotification(currentNotification)

                }

            }

        }
    }


}