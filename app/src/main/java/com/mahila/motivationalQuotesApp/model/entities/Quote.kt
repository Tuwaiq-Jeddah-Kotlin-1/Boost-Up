package com.mahila.motivationalQuotesApp.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Quote(
   // val quoteID: String,
    var text: String,
    val author: String,
   // val tag: String
) : Parcelable
