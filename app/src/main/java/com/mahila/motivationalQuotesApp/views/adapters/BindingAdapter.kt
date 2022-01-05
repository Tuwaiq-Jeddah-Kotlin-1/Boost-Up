package com.mahila.motivationalQuotesApp.views.adapters

import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.work.*
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.app.BoostUp
import com.mahila.motivationalQuotesApp.model.entities.Reminder
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.repository.FirebaseUserService
import com.mahila.motivationalQuotesApp.util.ReminderUtil.setReminder
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
                if (view.tag == "UNLIKE") {
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

        @BindingAdapter("android:updateReminderState")
        @JvmStatic
        fun updateReminderState(view: ImageView, currentReminder: Reminder) {
            if (!currentReminder.active) {
                view.tag = "INACTIVE"
                view.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_bell_gray
                    )
                )
            }
            view.setOnClickListener {
                println(currentReminder.active)
                println(view.tag.toString())
                if (view.tag == "ACTIVE") {
                    view.tag = "INACTIVE"
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_bell_gray
                        )
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        //update the activity field
                        FirebaseUserService.updateReminderState(currentReminder)
                    }
                    WorkManager.getInstance(view.context)
                        .cancelAllWorkByTag(currentReminder.reminderId)
                    Toast.makeText(
                        BoostUp.instant.applicationContext,
                        R.string.INACTIVE_msg,
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (view.tag == "INACTIVE") {
                    view.tag = "ACTIVE"
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_bell_purple
                        )


                    )
                   // view.animation=AnimationUtils.loadAnimation(view.context,R.anim.bell_anim)
                    GlobalScope.launch(Dispatchers.IO) {
                        FirebaseUserService.updateReminderState(currentReminder)

                    }
                    setReminder(
                        currentReminder.dateAndTime, currentReminder.randomQuote,
                        currentReminder.everyDay, currentReminder.reminderId, view
                    )
                    Toast.makeText(
                        BoostUp.instant.applicationContext,
                        R.string.ACTIVE_msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

    }


}