package com.mahila.motivationalQuotesApp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahila.motivationalQuotesApp.model.entities.GoQuotesData
import com.mahila.motivationalQuotesApp.model.repository.GoQuotesRepo
import kotlinx.coroutines.launch

private const val TAG = "QuotesViewModel"

class QuotesViewModel : ViewModel() {

    val goQuotesRepo = GoQuotesRepo()
    fun fetchQuotes(): LiveData<GoQuotesData> {

        val quotes = MutableLiveData<GoQuotesData>()
        viewModelScope.launch {
            try {
                quotes.postValue(goQuotesRepo.fetchQuotes())

            } catch (e: Exception) {
                Log.e(TAG, "Error fetching Quotes", e)
            }
        }
        return quotes
    }

}