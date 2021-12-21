package com.mahila.motivationalQuotesApp.model.entities

import android.os.Parcelable
import android.provider.ContactsContract
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val notificationId: String,
    val dateAndTime: Timestamp,
    val title: String
) : Parcelable {

    companion object {

        fun DocumentSnapshot.toNotification(): Notification? {
            return try {
                val notificationId = getString("notificationId")!!
                val dateAndTime = getTimestamp("dateAndTime")!!
                val title = getString("title")!!
                Notification(notificationId, dateAndTime, title)

            } catch (e: Exception) {
                Log.e(TAG, "Error converting toNotification", e)
                null
            }
        }

        private const val TAG = "Notification"
    }
}
