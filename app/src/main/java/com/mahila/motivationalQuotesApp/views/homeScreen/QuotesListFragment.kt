package com.mahila.motivationalQuotesApp.views.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mahila.motivationalQuotesApp.databinding.FragmentQuotesListBinding
import com.mahila.motivationalQuotesApp.viewModels.QuotesViewModel
import com.mahila.motivationalQuotesApp.views.adapters.QuotesRecycleViewAdapter


class QuotesListFragment : Fragment() {
    private val quotesViewModel: QuotesViewModel by viewModels()

    private var _binding: FragmentQuotesListBinding? = null
    private val binding get() = _binding!!

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
           //-----------old Api-----------
        /*   quotesViewModel.fetchQuotes().observe(viewLifecycleOwner, { dataQuotesList ->
               val listOfRandomQuotes = dataQuotesList.quotes.shuffled().take(20)
               val adapter = QuotesRecycleViewAdapter(listOfRandomQuotes)
               // val adapter=QuotesRecycleViewAdapter(quotesList=data.quotes)
               binding.quotesListRecycleView.adapter = adapter

           }) */


        // Setup RecyclerView
        // Observe LiveData
        setupRecyclerView()
        binding.refreshLayout.setOnRefreshListener {
            setupRecyclerView()
            binding.refreshLayout.isRefreshing = false
        }

    }

    private fun setupRecyclerView() {
        quotesViewModel.fetchQuotes().observe(viewLifecycleOwner, { dataQuotesList ->
            val listOfRandomQuotes = dataQuotesList.results.shuffled().take(5)
            val adapter = QuotesRecycleViewAdapter(listOfRandomQuotes)
            binding.quotesListRecycleView.adapter = adapter
            binding.quotesListRecycleView.scheduleLayoutAnimation()

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

