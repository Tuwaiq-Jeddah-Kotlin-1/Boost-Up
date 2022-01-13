package com.mahila.motivationalQuotesApp.model.entities

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reminder(
    val reminderId: String,
    val dateAndTime: Long,
    val delay: Long,
    var active: Boolean,
    val timeAsString: String,
    var dateAsString: String,
    val randomQuote: String,
    val everyDay: Boolean

) : Parcelable {
    companion object {

        fun DocumentSnapshot.toReminder(): Reminder? {
            return try {
                val reminderId = getString("reminderId")!!
                val dateAndTime = getLong("dateAndTime")!!
                val delay = getLong("delay")!!
                val active = getBoolean("active")!!
                val timeAsString = getString("timeAsString")!!
                val dateAsString = getString("dateAsString")!!
                val randomQuote = getString("randomQuote")!!
                val everyDay = getBoolean("everyDay")!!
                Reminder(
                    reminderId, dateAndTime, delay, active, timeAsString,
                    dateAsString, randomQuote, everyDay
                )

            } catch (e: Exception) {
                Log.e(TAG, "Error converting toReminder", e)
                null
            }
        }

        private const val TAG = "Reminder"
    }
}
