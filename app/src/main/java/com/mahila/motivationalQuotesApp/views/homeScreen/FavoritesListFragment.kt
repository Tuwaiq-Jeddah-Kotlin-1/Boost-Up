package com.mahila.motivationalQuotesApp.views.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentFavoritesListBinding
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.entities.Reminder
import com.mahila.motivationalQuotesApp.utils.OnItemClickListener
import com.mahila.motivationalQuotesApp.utils.addOnItemClickListener
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.adapters.FavoritesRecycleViewAdapter
import com.mahila.motivationalQuotesApp.views.adapters.NotificationRecycleViewAdapter
import com.mahila.motivationalQuotesApp.views.adapters.QuotesRecycleViewAdapter


class FavoritesListFragment : Fragment() {
    private var _binding: FragmentFavoritesListBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    lateinit var favoritesList: MutableList<Quote>
    lateinit var adapter: FavoritesRecycleViewAdapter
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
        //Refreshing
        binding.refreshLayout.setOnRefreshListener {
            setupRecyclerView()
            binding.refreshLayout.isRefreshing = false
        }
        //delete a quote from favoritesList
        binding.favoritesListRecycleView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                userViewModel.deleteFavoriteQuote(favoritesList[position])
                favoritesList[position].apply {
                    this.isLiked = !this.isLiked
                }
                favoritesList.removeAt(position)
                adapter = FavoritesRecycleViewAdapter(favoritesList)
                binding.favoritesListRecycleView.adapter = adapter
            }
        })

    }

    fun setupRecyclerView() {
        // Observe LiveData
        userViewModel.favoritesQuotes.observe(viewLifecycleOwner) {
            favoritesList = it.toMutableList()
            adapter = FavoritesRecycleViewAdapter(it)
            binding.favoritesListRecycleView.adapter = adapter
            binding.favoritesListRecycleView.scheduleLayoutAnimation()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}