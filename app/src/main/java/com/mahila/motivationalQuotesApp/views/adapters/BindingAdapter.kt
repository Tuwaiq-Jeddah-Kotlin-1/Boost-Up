package com.mahila.motivationalQuotesApp.views.adapters

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation.findNavController
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
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
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_unlike
                        )
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        FirebaseUserService.deleteFavoriteQuote(currentQuote)

                    }
                    if (view.tag == "LIKE") {
                        findNavController(view).navigate(R.id.action_favoriteFragment_to_favoriteFragment)
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
            view.setOnClickListener {
                WorkManager.getInstance(view.context)
                    .cancelAllWorkByTag(currentNotification.notificationId)
                if (currentNotification.active) {
                   // view.tag = "INACTIVE"
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

                }else{
                    println("-----INACTIVE---")
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_bell_purple
                        )
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        FirebaseUserService.setToInactiveNotification(currentNotification)
                        val currentTime = Calendar.getInstance().timeInMillis
                        val delay = currentNotification.dateAndTime - currentTime
                        val data = Data.Builder().putInt(NotificationWorker.NOTIFICATION_ID, 0).build()
                        val quoteNotification = Data.Builder().putString(
                            NotificationWorker.NOTIFICATION_CONTENT_ID,
                            currentNotification.randomQuote
                        ).build()
                        val notificationWork = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                            .addTag(currentNotification.notificationId)
                            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).setInputData(quoteNotification)
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
            if (!currentNotification.active) {
                view.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_bell_gray
                    )
                )
            }
        }

        @BindingAdapter("android:delNotificationIcon")
        @JvmStatic
        fun delNotificationIcon(view: View, currentNotification: Notification) {
            val currentTime = Calendar.getInstance().timeInMillis

            if (currentNotification.dateAndTime < currentTime) {

                view.visibility = View.GONE
                GlobalScope.launch(Dispatchers.IO) {
                    FirebaseUserService.deleteNotification(currentNotification)

                }

            }

        }
    }


}