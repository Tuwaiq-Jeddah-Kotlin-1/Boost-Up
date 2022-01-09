package com.mahila.motivationalQuotesApp.model.repositories

import QuotableData
import retrofit2.http.GET

interface GoQuotesAPIService {
    /* @GET("all?type=tag&val=motivational")
     suspend fun fetchQuotes(): GoQuotesData */

    @GET("quotes?tags=inspirational")
    suspend fun fetchQuotes(): QuotableData
}