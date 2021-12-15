package com.mahila.motivationalQuotesApp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mahila.motivationalQuotesApp.databinding.FragmentQuotesListBinding


class QuotesListFragment : Fragment() {


    private var _binding: FragmentQuotesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Data binding
        _binding = FragmentQuotesListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // Setup RecyclerView
        // Observe LiveData

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}