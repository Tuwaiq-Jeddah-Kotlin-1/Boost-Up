package com.mahila.motivationalQuotesApp.model.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GoQuotesBuilder {

    //private const val BASE_URL = "https://goquotes-api.herokuapp.com/api/v1/"
    private const val BASE_URL = "https://api.quotable.io/"
    private fun retrofit(): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

    val GO_QUOTES_API_SERVICE: GoQuotesAPIService = retrofit().create(GoQuotesAPIService::class.java)

}


