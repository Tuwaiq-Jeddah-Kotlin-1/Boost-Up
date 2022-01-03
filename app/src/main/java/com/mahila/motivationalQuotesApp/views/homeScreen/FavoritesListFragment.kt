package com.mahila.motivationalQuotesApp.views.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mahila.motivationalQuotesApp.R
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

        // binding
        _binding = FragmentFavoritesListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        setupRecyclerView()

        binding.refreshLayout.setOnRefreshListener {
            setupRecyclerView()
            findNavController().navigate(R.id.action_favoriteFragment_to_favoriteFragment)
            binding.refreshLayout.isRefreshing = false
        }
    }
fun setupRecyclerView(){
    // Observe LiveData
    userViewModel.favoritesQuotes.observe(viewLifecycleOwner, { favoritesList ->
        val adapter = FavoritesRecycleViewAdapter(favoritesList)
        binding.favoritesListRecycleView.adapter = adapter
    })
}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}