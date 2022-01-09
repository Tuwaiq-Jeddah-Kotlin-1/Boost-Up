package com.mahila.motivationalQuotesApp.model.repositories

import QuotableData
import retrofit2.http.GET

interface GoQuotesAPIService {

    @GET("quotes?tags=inspirational")
    suspend fun fetchQuotes(): QuotableData
}