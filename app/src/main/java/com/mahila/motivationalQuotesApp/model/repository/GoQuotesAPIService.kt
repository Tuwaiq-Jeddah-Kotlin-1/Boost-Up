package com.mahila.motivationalQuotesApp.model.repository

import com.mahila.motivationalQuotesApp.model.entities.GoQuotesData
import retrofit2.http.GET

interface GoQuotesAPIService {
    @GET("all?type=tag&val=motivational")
    suspend fun fetchQuotes(): GoQuotesData
}