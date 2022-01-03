package com.mahila.motivationalQuotesApp.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahila.motivationalQuotesApp.databinding.NotificationListItemBinding
import com.mahila.motivationalQuotesApp.model.entities.Notification

class NotificationRecycleViewAdapter(var notificationList:List<Notification>) :
    RecyclerView.Adapter< NotificationRecycleViewAdapter.QuotesHolder>() {
    class QuotesHolder(private val binding: NotificationListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.notification = notification
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
        return notificationList.size
    }

    override fun onBindViewHolder(holder: QuotesHolder, position: Int) {
        val currentNotification = notificationList[position]
       // holder.itemView.
        holder.bind(currentNotification)
    }




}



