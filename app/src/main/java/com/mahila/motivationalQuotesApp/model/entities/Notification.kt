package com.mahila.motivationalQuotesApp.model.entities

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val notificationId: String,
    var dateAndTime: Long,
    var delay: Long,
    var type: String,
    var active: Boolean,
    var timeAsString: String,
    var dateAsString: String
     , var randomQuote:String

) : Parcelable {

    companion object {

        fun DocumentSnapshot.toNotification(): Notification? {
            return try {
                val notificationId = getString("notificationId")!!
                val dateAndTime = getLong("dateAndTime")!!
                val delay = getLong("delay")!!
                val type = getString("type")!!
                val active = getBoolean("active")!!
                val timeAsString = getString("timeAsString")!!
                val dateAsString = getString("dateAsString")!!
               val randomQuote = getString("randomQuote")!!
                Notification(
                    notificationId, dateAndTime, delay, type, active, timeAsString, dateAsString
                    ,randomQuote)

            } catch (e: Exception) {
                Log.e(TAG, "Error converting toNotification", e)
                null
            }
        }

        private const val TAG = "Notification"
    }
}
