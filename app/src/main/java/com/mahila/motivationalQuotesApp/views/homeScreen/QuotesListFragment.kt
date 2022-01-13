package com.mahila.motivationalQuotesApp.views.homeScreen

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentQuotesListBinding
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.utils.OnItemClickListener
import com.mahila.motivationalQuotesApp.utils.NetworkConnectionUtil
import com.mahila.motivationalQuotesApp.utils.addOnItemClickListener
import com.mahila.motivationalQuotesApp.viewModels.QuotesViewModel
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.SHARED_LANG_KEY
import com.mahila.motivationalQuotesApp.views.SHARED_STAY_SIGNED_IN
import com.mahila.motivationalQuotesApp.views.adapters.QuotesRecycleViewAdapter
import com.mahila.motivationalQuotesApp.views.sharePreferencesValueOfLang
import com.mahila.motivationalQuotesApp.views.sharedPre


class QuotesListFragment : Fragment() {
    private val quotesViewModel: QuotesViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentQuotesListBinding? = null
    private val binding get() = _binding!!
    lateinit var quotesList: MutableList<Quote>
    lateinit var adapter: QuotesRecycleViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //  binding
        _binding = FragmentQuotesListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //check network connection.
        if (NetworkConnectionUtil.isNetworkConnected(requireContext())) {
            // Setup RecyclerView
            setupRecyclerView()
        } else {
            binding.quotesListRecycleView.visibility = View.GONE

            binding.networkConnectedIcon.visibility = View.VISIBLE
            binding.tvNetworkConnected.visibility = View.VISIBLE
        }

        // Refresh RecyclerView
        binding.refreshLayout.setOnRefreshListener {
            setupRecyclerView()
            binding.refreshLayout.isRefreshing = false
        }
        // Favouring a quote
        binding.quotesListRecycleView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                if (!quotesList[position].isLiked) {
                    userViewModel.addFavoriteQuote(quotesList[position])
                } else {
                    userViewModel.deleteFavoriteQuote(quotesList[position])
                }
                quotesList[position].apply {
                    this.isLiked = !this.isLiked
                }
                adapter = QuotesRecycleViewAdapter(quotesList)
                binding.quotesListRecycleView.adapter = adapter
                binding.quotesListRecycleView.layoutManager?.scrollToPosition(position)
            }


        })

    }

    private fun setupRecyclerView() {
        // Observe LiveData
        if (currentLang() == "en") {
            quotesViewModel.fetchQuotes().observe(viewLifecycleOwner) { dataQuotesList ->
                quotesList = dataQuotesList.toMutableList()
                adapter = QuotesRecycleViewAdapter(dataQuotesList)
                binding.quotesListRecycleView.adapter = adapter
                binding.quotesListRecycleView.scheduleLayoutAnimation()
            }
        } else {
            quotesViewModel.fetchARQuotes().observe(viewLifecycleOwner) { dataQuotesList ->
                quotesList = dataQuotesList.toMutableList()
                adapter = QuotesRecycleViewAdapter(dataQuotesList)
                binding.quotesListRecycleView.adapter = adapter
                binding.quotesListRecycleView.scheduleLayoutAnimation()

            }
        }

    }

    private fun currentLang(): String {
        sharePreferencesValueOfLang = sharedPre.getString(SHARED_LANG_KEY, "Auto")
        return when (sharePreferencesValueOfLang) {
            "en" -> "en"
            "Auto" ->
                ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0).language

            else -> "ar"
        }
    }

    override fun onStart() {
        super.onStart()
        if (!sharedPre.getBoolean(SHARED_STAY_SIGNED_IN, false) ||
            !userViewModel.checkSignInState()
        ) {
            findNavController().navigate(R.id.action_quotesFragment_to_signinFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

