package com.mahila.motivationalQuotesApp.model.entities

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Quote(
    @SerializedName("content")
    var text: String,
    val author: String,
    var isLiked:Boolean=false
) : Parcelable {

    companion object {

        fun DocumentSnapshot.toQuote(): Quote? {
            return try {
                val text = getString("text")!!
                val author = getString("author")!!
                Quote(text, author)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting toQuote", e)
                null
            }
        }

        private const val TAG = "Quote"
    }
}
//