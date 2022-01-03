package com.mahila.motivationalQuotesApp.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FavoritesListItemBinding
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.repository.FirebaseUserService
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoritesRecycleViewAdapter(var quotesList:List<Quote>) :
    RecyclerView.Adapter< FavoritesRecycleViewAdapter.QuotesHolder>() {
    class QuotesHolder(private val binding: FavoritesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quote: Quote) {
            binding.quote = quote
            binding.likeIcon.setOnClickListener {
                binding.likeIcon.tag = "UNLIKE"
                binding.likeIcon.setImageDrawable(
                    AppCompatResources.getDrawable(
                        binding.likeIcon.context,
                        R.drawable.ic_unlike
                    )
                )
                GlobalScope.launch(Dispatchers.IO) {
                    FirebaseUserService.deleteFavoriteQuote(quote)

                }
                binding.cl1.visibility=View.GONE

            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): QuotesHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoritesListItemBinding.inflate(layoutInflater, parent, false)

                return QuotesHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesHolder {
        return QuotesHolder.from(
            parent
        )
    }

    override fun getItemCount(): Int {
        return quotesList.size
    }

    override fun onBindViewHolder(holder: QuotesHolder, position: Int) {
        val currentQuote = quotesList[position]

        holder.bind(currentQuote)
    }




}



