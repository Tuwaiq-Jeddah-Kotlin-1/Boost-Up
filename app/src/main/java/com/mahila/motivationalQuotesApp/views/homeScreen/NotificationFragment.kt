package com.mahila.motivationalQuotesApp.views.homeScreen

import NotificationWorker
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.mahila.motivationalQuotesApp.databinding.FragmentNotificationBinding
import java.util.concurrent.TimeUnit

class NotificationFragment : Fragment() {

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
        binding.addingBtn.setOnClickListener {
            /*   //under test 
               val c= Calendar.getInstance()
               c.set(year, month, day ,hour, minute,0)
               val today=Calendar.getInstance()
               val diff=(c.timeInMillis/1000L)-(today.timeInMillis/1000L).toInt()


                //  NotificationHelper(view.context).createNotification("TRY it", "A new X has been ...")
                val myw = OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(
                    diff,
                    TimeUnit.SECONDS
                )
                    .setInputData(
                        workDataOf(
                            "title" to "Quote of the day",
                            "msg" to "Quote"
                        )
                    ).build()
                WorkManager.getInstance(requireContext()).enqueue(myw)*/

           
            }

        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }