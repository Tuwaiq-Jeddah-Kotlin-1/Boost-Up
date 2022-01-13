package com.mahila.motivationalQuotesApp.views.adapters

import android.content.Intent
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import com.mahila.motivationalQuotesApp.model.entities.Quote

class BindingAdapters {
    companion object {

        @BindingAdapter("android:shareAQuoteViaOtherApps")
        @JvmStatic
        fun shareAQuoteViaOtherApps(view: ImageView, currentQuote: Quote) {
            view.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${currentQuote.text} \n\n${currentQuote.author}")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Share this quote with: ")
                startActivity(view.context, shareIntent, null)

            }
        }


    }


}