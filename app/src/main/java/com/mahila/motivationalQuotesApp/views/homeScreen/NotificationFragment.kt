package com.mahila.motivationalQuotesApp.views.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentNotificationBinding
import com.mahila.motivationalQuotesApp.model.entities.Reminder
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.adapters.NotificationRecycleViewAdapter
import java.util.*

class NotificationFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // binding
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setup RecyclerView
        setupRecyclerView()

        binding.addingBtn.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_notificationFragment_to_addNotificationFragment)
        }
        binding.refreshLayout.setOnRefreshListener {
            // Setup RecyclerView
            setupRecyclerView()
            findNavController().navigate(R.id.action_notificationFragment_to_notificationFragment)
            binding.refreshLayout.isRefreshing = false
        }

    }

    private fun setupRecyclerView() {
        // Observe LiveData
        userViewModel.notifications.observe(viewLifecycleOwner, { _reminderList ->
            val reminderList = deleteEndedReminders(_reminderList)
            val adapter = NotificationRecycleViewAdapter(reminderList)
            binding.notificationListRecycleView.adapter = adapter
            binding.notificationListRecycleView.scheduleLayoutAnimation()
        })

    }

    private fun deleteEndedReminders(reminderList: List<Reminder>):
            List<Reminder> {
        val currentTime = Calendar.getInstance().timeInMillis
        return reminderList.filter {
             it.dateAndTime >= currentTime
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}