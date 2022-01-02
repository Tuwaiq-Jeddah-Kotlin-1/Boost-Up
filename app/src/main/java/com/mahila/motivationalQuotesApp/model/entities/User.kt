package com.mahila.motivationalQuotesApp.model.entities

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String,
    var name: String,
    var email: String,
) : Parcelable {

    companion object {
        fun DocumentSnapshot.toUser(): User? {
            return try {
                val uid = getString("uid")!!
                val name = getString("name")!!
                val email = getString("email")!!
                User(uid, name, email)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting toUser", e)
                null
            }
        }

        private const val TAG = "User"
    }
}


