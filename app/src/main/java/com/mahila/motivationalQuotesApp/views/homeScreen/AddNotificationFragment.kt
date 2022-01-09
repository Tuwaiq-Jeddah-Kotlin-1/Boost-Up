package com.mahila.motivationalQuotesApp.views.homeScreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.timepicker.TimeFormat
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.FragmentAddNotificationBinding
import com.mahila.motivationalQuotesApp.model.entities.Reminder
import com.mahila.motivationalQuotesApp.utils.ReminderUtil
import com.mahila.motivationalQuotesApp.viewModels.QuotesViewModel
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import java.util.*


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
    private var randomQuote = ""
    private var everyDay: Boolean = false
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
            val listOfRandomQuotes = quotesList.shuffled().take(1)
            randomQuote = "${listOfRandomQuotes[0].text}*${listOfRandomQuotes[0].author}"
        })

        binding.datePicker.setOnClickListener {
            OpenDatePiecker()
        }
        binding.timePicker.setOnClickListener {
            OpenTimePiecker()
        }

        binding.btnOk.setOnClickListener {
            if (binding.isEveryDay.isChecked) {
                dateAsString = getString(R.string.daily)
                everyDay = true
            }
            if (selectedDday == 0) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.time_fields_cannot_be_empty),
                    Toast.LENGTH_LONG
                )
                    .show()
            } else if (selectedYear == 0) {
                Toast.makeText(
                    requireContext(), getString(R.string.date_fields_cannot_be_empty),
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                setUpReminder(view)
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
        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            binding.root.context, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                selectedHour = hour
                selectedMinute = minute
                timeAsString = "$selectedHour:$selectedMinute:00"
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
        )
        timePicker.show()
    }

    private fun OpenDatePiecker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            binding.root.context,
            { _, year, monthOfYear, dayOfMonth ->
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

    fun setUpReminder(view: View) {
        var customCalendar = Calendar.getInstance()
        customCalendar.set(
            selectedYear,
            selectedMonth,
            selectedDday,
            selectedHour,
            selectedMinute,
            0
        )
        val reminderTime = customCalendar.timeInMillis
        val currentTime = Calendar.getInstance().timeInMillis
        if (reminderTime > currentTime) {
            val delay = reminderTime - currentTime
            scheduleReminder(delay, view, reminderTime)
            Toast.makeText(
                requireContext(),
                getString(R.string.Reminder_scheduled_successfully),
                Toast.LENGTH_LONG
            ).show()

        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.invalid_time_date),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun scheduleReminder(delay: Long, view: View, reminderTime: Long) {
        val reminderId = UUID.randomUUID()
        //create reminder
        ReminderUtil.setReminder(
            reminderTime, randomQuote,
            everyDay, reminderId.toString(), view
        )
        //add reminder to FireStore
        userViewModel.addReminder(
            Reminder(
                reminderId.toString(),
                reminderTime,
                delay,
                true,
                timeAsString,
                dateAsString, randomQuote, everyDay
            )
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}