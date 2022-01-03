package com.mahila.motivationalQuotesApp.model.repository

import QuotableData
import com.mahila.motivationalQuotesApp.model.entities.GoQuotesData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoQuotesRepo {
    private val goQuotesAPI = GoQuotesBuilder.GO_QUOTES_API_SERVICE
    /*suspend fun fetchQuotes(): GoQuotesData =
        withContext(Dispatchers.IO) { goQuotesAPI.fetchQuotes() }*/

    suspend fun fetchQuotes(): QuotableData  =
        withContext(Dispatchers.IO) { goQuotesAPI.fetchQuotes() }
}