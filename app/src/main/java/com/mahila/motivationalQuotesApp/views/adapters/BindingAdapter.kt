package com.mahila.motivationalQuotesApp.views.adapters

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.work.WorkManager
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.model.entities.Notification
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.repository.FirebaseUserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

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
            view.setOnClickListener {
                WorkManager.getInstance(view.context)
                    .cancelAllWorkByTag(currentNotification.notificationId)
                if (view.tag != "INACTIVE") {
                    view.tag = "INACTIVE"
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_bell_gray
                        )
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        FirebaseUserService.setToInactiveNotification(currentNotification)

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