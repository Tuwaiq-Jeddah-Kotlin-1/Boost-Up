package com.mahila.motivationalQuotesApp.views.homeScreen

import com.mahila.motivationalQuotesApp.worker.NotificationWorker
import com.mahila.motivationalQuotesApp.worker.NotificationWorker.Companion.NOTIFICATION_CONTENT_ID
import com.mahila.motivationalQuotesApp.worker.NotificationWorker.Companion.NOTIFICATION_ID
import com.mahila.motivationalQuotesApp.worker.NotificationWorker.Companion.NOTIFICATION_WORK
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentAddNotificationBinding
import com.mahila.motivationalQuotesApp.model.entities.Notification
import com.mahila.motivationalQuotesApp.viewModels.QuotesViewModel
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import java.util.*
import java.util.concurrent.TimeUnit


class AddNotificationFragment : Fragment() {

    private var _binding: FragmentAddNotificationBinding? = null
    private val binding get() = _binding!!
    private var selectedDday: Int = 0
    private var selectedMonth: Int = 0
    private var selectedYear: Int = 0
    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0
    var timeAsString = ""
    var dateAsString = ""
    private var randomQuote =""
    private var notificationType: String = getString(R.string.onetime_reminder)
    private val userViewModel: UserViewModel by viewModels()
    private val quoteViewModel: QuotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNotificationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //

        quoteViewModel.fetchQuotes().observe(viewLifecycleOwner, { quotesList ->
            val listOfRandomQuotes = quotesList.results.shuffled().take(1)
            randomQuote="${listOfRandomQuotes[0].text}*${listOfRandomQuotes[0].author}"
        })

        binding.datePicker.setOnClickListener {
            OpenDatePiecker()
        }
        binding.timePicker.setOnClickListener {
            OpenTimePiecker()
        }

        binding.btnOk.setOnClickListener {
            /*if (binding.isEveryDay.isChecked) {
                notificationType = "Daily Reminder"
            }*/
            if (selectedDday == 0) {
                Toast.makeText(requireContext(), getString(R.string.time_fields_cannot_be_empty)
                    , Toast.LENGTH_LONG)
                    .show()
            } else if (selectedYear == 0) {
                Toast.makeText(requireContext(), getString(R.string.date_fields_cannot_be_empty),
                    Toast.LENGTH_LONG)
                    .show()
            } else {
                setUpNotification()

                findNavController()
                    .navigate(R.id.action_addNotificationFragment_to_notificationFragment)
            }

        }
        binding.btnCan.setOnClickListener {
            findNavController()
                .navigate(R.id.action_addNotificationFragment_to_notificationFragment)
        }

    }

    private fun OpenTimePiecker() {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText(getString(R.string.select_Reminder_time))
                .setInputMode(INPUT_MODE_KEYBOARD)
                .setTimeFormat(clockFormat)
                .build()


        timePicker.show(parentFragmentManager, "Time")
        timePicker.addOnNegativeButtonClickListener {

        }
        timePicker.addOnPositiveButtonClickListener {
            selectedHour = timePicker.hour
            selectedMinute = timePicker.minute
            timeAsString = "$selectedHour:$selectedMinute:00"
        }

    }

    private fun OpenDatePiecker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            binding.root.context,
            { view, year, monthOfYear, dayOfMonth ->
                selectedDday = dayOfMonth
                selectedMonth = monthOfYear
                selectedYear = year
                dateAsString = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        //-----------------set to setMinDate
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis())
        datePickerDialog.show()

    }

    fun setUpNotification() {
        var customCalendar = Calendar.getInstance()
        customCalendar.set(
            selectedYear,
            selectedMonth,
            selectedDday,
            selectedHour,
            selectedMinute,
            0
        )
        val notificationTime = customCalendar.timeInMillis
        val currentTime = Calendar.getInstance().timeInMillis
       // println(customCalendar.time)
       // println(Calendar.getInstance().time)
        if (notificationTime > currentTime) {
            val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()
            val delay = notificationTime - currentTime
            scheduleNotification(delay, data, notificationTime)
            Toast.makeText(requireContext(), getString(R.string.Reminder_scheduled_successfully), Toast.LENGTH_LONG)
                .show()

        } else {
            Toast.makeText(requireContext(), getString(R.string.invalid_time_date), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun scheduleNotification(delay: Long, data: Data, notificationTime: Long) {
        val notificationId = UUID.randomUUID()
        val quoteNotification = Data.Builder().putString(
            NOTIFICATION_CONTENT_ID,
            randomQuote
        ).build()

        val notificationWork = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .addTag(notificationId.toString())
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).setInputData(quoteNotification)
            .build()

        val instanceWorkManager = WorkManager.getInstance(requireContext())
        instanceWorkManager.beginUniqueWork(
            NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE,
            notificationWork
        ).enqueue()

        //add Notification to FireStore

        userViewModel.addNotification(
            Notification(
                notificationId.toString(),
                notificationTime,
                delay,
                notificationType,
                true,
                timeAsString,
                dateAsString,
                randomQuote
            )
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}