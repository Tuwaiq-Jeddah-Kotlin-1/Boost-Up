package com.mahila.motivationalQuotesApp.views.homeScreen

import NotificationWorker
import NotificationWorker.Companion.NOTIFICATION_ID
import NotificationWorker.Companion.NOTIFICATION_WORK
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.ExistingWorkPolicy.REPLACE
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentNotificationBinding
import com.mahila.motivationalQuotesApp.viewModels.QuotesViewModel
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.adapters.NotificationRecycleViewAdapter
import com.mahila.motivationalQuotesApp.views.adapters.QuotesRecycleViewAdapter
import java.lang.System.currentTimeMillis
import java.util.*
import java.util.concurrent.TimeUnit.MILLISECONDS

class NotificationFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Data binding
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // Setup RecyclerView
        // Observe LiveData
        return binding.root
    }
//

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setup RecyclerView
        // Observe LiveData
        userViewModel.notifications.observe(viewLifecycleOwner, { notificationsList ->
            val adapter = NotificationRecycleViewAdapter(notificationsList)
            binding.notificationListRecycleView.adapter = adapter

        })
        binding.addingBtn.setOnClickListener {

            view.findNavController()
                .navigate(R.id.action_notificationFragment_to_addNotificationFragment)
        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}