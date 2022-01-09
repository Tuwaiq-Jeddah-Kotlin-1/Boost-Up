package com.mahila.motivationalQuotesApp.views.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.WorkManager
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentQuotesListBinding
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.entities.Reminder
import com.mahila.motivationalQuotesApp.utils.OnItemClickListener
import com.mahila.motivationalQuotesApp.utils.ReminderUtil
import com.mahila.motivationalQuotesApp.utils.addOnItemClickListener
import com.mahila.motivationalQuotesApp.viewModels.QuotesViewModel
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.SHARED_STAY_SIGNED_IN
import com.mahila.motivationalQuotesApp.views.adapters.NotificationRecycleViewAdapter
import com.mahila.motivationalQuotesApp.views.adapters.QuotesRecycleViewAdapter
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

        // Setup RecyclerView
        setupRecyclerView()

        // Refresh RecyclerView
        binding.refreshLayout.setOnRefreshListener {
            setupRecyclerView()
            binding.refreshLayout.isRefreshing = false
        }
        // Favouring a quote
        binding.quotesListRecycleView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (view.tag != "LIKE") {
                    view.tag = "LIKE"
                    userViewModel.addFavoriteQuote(quotesList[position])
                }else{
                    view.tag = "UNLIKE"
                    userViewModel.deleteFavoriteQuote(quotesList[position])

                }
                quotesList[position].apply {
                    this.isLiked = !this.isLiked
                }
                adapter = QuotesRecycleViewAdapter(quotesList)
                binding.quotesListRecycleView.adapter = adapter
            }


        })

    }

    private fun setupRecyclerView() {
        // Observe LiveData
        quotesViewModel.fetchQuotes().observe(viewLifecycleOwner) { dataQuotesList ->
            quotesList = dataQuotesList.toMutableList()
            adapter = QuotesRecycleViewAdapter(dataQuotesList)
            binding.quotesListRecycleView.adapter = adapter
            binding.quotesListRecycleView.scheduleLayoutAnimation()

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

