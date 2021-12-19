package com.mahila.motivationalQuotesApp.model.entities

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize


@Parcelize
data class Quote(
   // val quoteID: String,
    var text: String,
    val author: String,
   // val tag: String
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