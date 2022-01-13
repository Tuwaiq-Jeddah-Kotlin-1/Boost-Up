package com.mahila.motivationalQuotesApp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.repositories.GoQuotesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "QuotesViewModel"

class QuotesViewModel : ViewModel() {

    fun fetchQuotes(): LiveData<List<Quote>> {

        val quotes = MutableLiveData<List<Quote>>()
        viewModelScope.launch(Dispatchers.IO)  {
            try {
                quotes.postValue(GoQuotesRepo.fetchQuotes())
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching Quotes", e)
            }
        }
        return quotes
    }   fun fetchARQuotes(): LiveData<List<Quote>> {

        val quotes = MutableLiveData<List<Quote>>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                quotes.postValue(GoQuotesRepo.fetchARQuotes())
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching ARQuotes", e)
            }
        }
        return quotes
    }

}