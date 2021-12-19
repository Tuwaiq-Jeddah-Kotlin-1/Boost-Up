package com.mahila.motivationalQuotesApp.views.adapters

import android.content.Intent
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.repository.FirebaseUserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BindingAdapters {
    companion object {

        @BindingAdapter("android:sendAnQuoteToFavoritesList")
        @JvmStatic
        fun sendAnQuoteToFavoritesList(view: ImageView, currentQuote: Quote) {
            view.setOnClickListener {
                if (view.tag != "LIKE") {
                    view.tag = "LIKE"
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_like
                        )
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        FirebaseUserService.addFavoriteQuote(currentQuote)

                    }

                } else {
                    view.tag = "UNLIKE"
                    view.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_unlike
                        )
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        FirebaseUserService.deleteFavoriteQuote(currentQuote)

                    }
                }

            }
        }

        @BindingAdapter("android:shareAnQuoteViaOtherApps")
        @JvmStatic
        fun shareAnQuoteViaOtherApps(view: ImageView, currentQuote: Quote) {
            view.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${currentQuote.text} \n\n${currentQuote.author}")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, "Share this quote with: ")
                startActivity(view.context,shareIntent,null)

            }
        }

    }

}