package com.mahila.motivationalQuotesApp.views.homeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.work.WorkManager
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentNotificationBinding
import com.mahila.motivationalQuotesApp.model.entities.Reminder
import com.mahila.motivationalQuotesApp.utils.OnItemClickListener
import com.mahila.motivationalQuotesApp.utils.ReminderUtil.setReminder
import com.mahila.motivationalQuotesApp.utils.NetworkConnectionUtil
import com.mahila.motivationalQuotesApp.utils.addOnItemClickListener
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.adapters.NotificationRecycleViewAdapter
import java.util.*

class NotificationFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    lateinit var reminderList: MutableList<Reminder>
    lateinit var adapter: NotificationRecycleViewAdapter
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
        //check network connection.
        if (NetworkConnectionUtil.isNetworkConnected(requireContext())) {
            // Setup RecyclerView
            setupRecyclerView()
        } else {
            binding.notificationListRecycleView.visibility = View.GONE
            binding.tvNetworkConnected.visibility = View.VISIBLE

            binding.networkConnectedIcon.visibility = View.VISIBLE
        }

        binding.addingBtn.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_notificationFragment_to_addNotificationFragment)
        }

        // Refresh RecyclerView
        binding.refreshLayout.setOnRefreshListener {
            // Setup RecyclerView
            setupRecyclerView()
            binding.refreshLayout.isRefreshing = false
        }

        binding.notificationListRecycleView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val currentTime = Calendar.getInstance().timeInMillis
                if (!reminderList[position].active && reminderList[position].everyDay
                    && reminderList[position].dateAndTime < currentTime
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.REACTIVE_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!reminderList[position].active) {
                    setReminder(
                        reminderList[position].dateAndTime, reminderList[position].randomQuote,
                        reminderList[position].everyDay, reminderList[position].reminderId
                    )
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.ACTIVE_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                    updateReminderState(position)
                } else {
                    WorkManager.getInstance(view.context)
                        .cancelAllWorkByTag(reminderList[position].reminderId)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.INACTIVE_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                    updateReminderState(position)
                }


            }


        })

    }

    private fun updateReminderState(position: Int) {
        userViewModel.updateReminderState(reminderList[position])
        reminderList[position].apply {
            this.active = !this.active
        }
        adapter = NotificationRecycleViewAdapter(reminderList)
        binding.notificationListRecycleView.adapter = adapter
    }


    private fun setupRecyclerView() {
        // Observe LiveData
        userViewModel.notifications.observe(viewLifecycleOwner) { _reminderList ->
            reminderList = deleteEndedReminders(_reminderList).toMutableList()
            adapter = NotificationRecycleViewAdapter(reminderList)
            binding.notificationListRecycleView.adapter = adapter
            binding.notificationListRecycleView.scheduleLayoutAnimation()
        }

    }

    private fun deleteEndedReminders(_reminderList: List<Reminder>): List<Reminder> {
        val currentTime = Calendar.getInstance().timeInMillis
        val reminderList = mutableListOf<Reminder>()
        _reminderList.forEach {
            if (!(it.dateAndTime >= currentTime || (it.everyDay && it.active))) {
                userViewModel.deleteReminder(it)

            } else {
                reminderList.add(it)
            }

        }
        return reminderList

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}