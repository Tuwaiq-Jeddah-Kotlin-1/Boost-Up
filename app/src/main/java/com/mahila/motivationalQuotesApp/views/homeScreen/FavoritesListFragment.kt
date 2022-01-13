package com.mahila.motivationalQuotesApp.views.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentFavoritesListBinding
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.utils.OnItemClickListener
import com.mahila.motivationalQuotesApp.utils.NetworkConnectionUtil.isNetworkConnected
import com.mahila.motivationalQuotesApp.utils.addOnItemClickListener
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.adapters.FavoritesRecycleViewAdapter


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
        //
        if (isNetworkConnected(requireContext())) {
            // Setup RecyclerView
            setupRecyclerView()
        } else {
            binding.favoritesListRecycleView.visibility = View.GONE
            binding.tvNetworkConnected.visibility = View.VISIBLE
            binding.networkConnectedIcon.visibility = View.VISIBLE
        }


        //delete a quote from favoritesList
        binding.favoritesListRecycleView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val favoriteQuote = favoritesList[position]
                userViewModel.deleteFavoriteQuote(favoritesList[position])
                favoritesList.removeAt(position)
                adapter = FavoritesRecycleViewAdapter(favoritesList)
                binding.favoritesListRecycleView.adapter = adapter
                val snackBar = Snackbar.make(
                    binding.favoritesListRecycleView, getString(R.string.like_removed),
                    Snackbar.LENGTH_LONG
                )
                snackBar.setAction(getString(R.string.undo)) {

                    userViewModel.addFavoriteQuote(favoriteQuote)
                    favoritesList.add(position, favoriteQuote)
                    adapter = FavoritesRecycleViewAdapter(favoritesList)
                    binding.favoritesListRecycleView.adapter = adapter
                    binding.favoritesListRecycleView.layoutManager?.scrollToPosition(position)

                }
                snackBar.show()
            }
        })

    }

    private fun setupRecyclerView() {
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