package com.mahila.motivationalQuotesApp.model.entities

data class GoQuotesData(
    val status: Int,
    val message: String,
    val count: Int,
    val quotes: List<Quote>
)
