package com.mahila.motivationalQuotesApp.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahila.motivationalQuotesApp.databinding.FavoritesListItemBinding
import com.mahila.motivationalQuotesApp.model.entities.Quote

class FavoritesRecycleViewAdapter(var quotesList: List<Quote>) :
    RecyclerView.Adapter<FavoritesRecycleViewAdapter.QuotesHolder>() {
    class QuotesHolder(private val binding: FavoritesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quote: Quote) {
            binding.quote = quote
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




