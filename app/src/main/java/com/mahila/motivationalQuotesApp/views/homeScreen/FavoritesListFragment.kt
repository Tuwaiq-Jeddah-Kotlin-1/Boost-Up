package com.mahila.motivationalQuotesApp.views.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mahila.motivationalQuotesApp.databinding.FragmentFavoritesListBinding
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.adapters.FavoritesRecycleViewAdapter


class FavoritesListFragment : Fragment() {
    private var _binding: FragmentFavoritesListBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Data binding
        _binding = FragmentFavoritesListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        // Observe LiveData
        userViewModel.favoritesQuotes.observe(viewLifecycleOwner, { favoritesList ->
           val adapter= FavoritesRecycleViewAdapter(favoritesList)
            
            binding.favoritesListRecycleView.adapter = adapter

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}