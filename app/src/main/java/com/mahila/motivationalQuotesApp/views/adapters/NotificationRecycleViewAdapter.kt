package com.mahila.motivationalQuotesApp.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahila.motivationalQuotesApp.databinding.NotificationListItemBinding
import com.mahila.motivationalQuotesApp.model.entities.Reminder
import com.mahila.motivationalQuotesApp.model.repository.FirebaseUserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class NotificationRecycleViewAdapter(var reminderList: List<Reminder>) :
    RecyclerView.Adapter<NotificationRecycleViewAdapter.QuotesHolder>() {
    class QuotesHolder(private val binding: NotificationListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: Reminder) {
            binding.reminder = reminder
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): QuotesHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NotificationListItemBinding.inflate(layoutInflater, parent, false)

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
        return reminderList.size
    }

    override fun onBindViewHolder(holder: QuotesHolder, position: Int) {
        val currentNotification = reminderList[position]
        holder.bind(currentNotification)
    }


}



