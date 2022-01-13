package com.mahila.motivationalQuotesApp.model.repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.entities.Quote.Companion.toQuote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object GoQuotesRepo {
    private const val TAG = "GoQuotesRepo"
    private val goQuotesAPI = GoQuotesBuilder.GO_QUOTES_API_SERVICE

    suspend fun fetchQuotes(): List<Quote> =
        withContext(Dispatchers.IO) { goQuotesAPI.fetchQuotes().results.shuffled().take(5) }

    suspend fun fetchARQuotes(): List<Quote>? =
        withContext(Dispatchers.IO) {
            val db = FirebaseFirestore.getInstance()
            try {
                db.collection("arabicquote").get().await()
                    .documents.mapNotNull {
                        it.toQuote()
                            }.shuffled().take(5)

            } catch (e: Exception) {
                Log.e(TAG, "Error getting the ARQuotes list", e)
                null
            }
        }
}