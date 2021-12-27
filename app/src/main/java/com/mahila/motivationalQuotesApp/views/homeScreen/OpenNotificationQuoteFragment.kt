package com.mahila.motivationalQuotesApp.views.homeScreen

import NotificationWorker.Companion.NOTIFICATION_CONTENT_ID
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentOpenNotificationQuoteBinding


class OpenNotificationQuoteFragment : Fragment() {
    private var _binding: FragmentOpenNotificationQuoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Data binding
        _binding = FragmentOpenNotificationQuoteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var quote = arguments?.getString(NOTIFICATION_CONTENT_ID)?.split(",")

        val quoteText = quote?.get(0) ?: getString(R.string.you_can)
        val quoteAuthor = quote?.get(1) ?: getString(R.string.me)
        binding.quoteText.text = quoteText
        binding.quoteAuthor.text = quoteAuthor
        binding.shareIcon.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "$quoteText \n\n$quoteAuthor")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, getString(R.string.Share_quote_with))
            ContextCompat.startActivity(view.context, shareIntent, null)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}