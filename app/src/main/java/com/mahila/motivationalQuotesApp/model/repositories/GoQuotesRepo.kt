package com.mahila.motivationalQuotesApp.model.repositories

import com.mahila.motivationalQuotesApp.model.entities.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoQuotesRepo {
    private val goQuotesAPI = GoQuotesBuilder.GO_QUOTES_API_SERVICE

    suspend fun fetchQuotes(): List<Quote> =
        withContext(Dispatchers.IO) { goQuotesAPI.fetchQuotes().results.shuffled().take(5) }
}