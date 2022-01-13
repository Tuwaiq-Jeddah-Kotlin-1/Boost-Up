package com.mahila.motivationalQuotesApp.viewModels

import android.view.View
import androidx.lifecycle.*
import com.mahila.motivationalQuotesApp.model.entities.Reminder
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.entities.User
import com.mahila.motivationalQuotesApp.model.repositories.FirebaseUserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserViewModel(): ViewModel()
{
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _FavoritesQuotes = MutableLiveData<List<Quote>>()
    val favoritesQuotes: LiveData<List<Quote>> = _FavoritesQuotes
    private val _Notifications = MutableLiveData<List<Reminder>>()
    val notifications: LiveData<List<Reminder>> = _Notifications

    init {
        viewModelScope.launch {
            _user.value = FirebaseUserService.getUserData()
        }
        viewModelScope.launch {
            _FavoritesQuotes.value = FirebaseUserService.getFavoritesQuotes()
        }
        viewModelScope.launch {
            _Notifications.value = FirebaseUserService.getReminders()
        }

    }

    fun signUp(name: String, email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        FirebaseUserService.signUp(name, email, password)
    }

    fun signIn(email: String, password: String,view: View) =
        viewModelScope.launch { FirebaseUserService.signIn(email, password,view) }

    fun signOut() = viewModelScope.launch {
        FirebaseUserService.signOut()
    }

    fun forgotPassword(email: String) = viewModelScope.launch(Dispatchers.IO) {
        FirebaseUserService.forgotPassword(email)

    }

    fun checkSignInState() = FirebaseUserService.checkSignInState()

    fun resetUserName(newUserName: String) = viewModelScope.launch(Dispatchers.IO) {
        FirebaseUserService.resetUserName(newUserName)

    }

    fun addFavoriteQuote(quote: Quote) = viewModelScope.launch(Dispatchers.IO) {
        FirebaseUserService.addFavoriteQuote(quote)

    }
    fun deleteFavoriteQuote(quote: Quote) = viewModelScope.launch(Dispatchers.IO) {
        FirebaseUserService.deleteFavoriteQuote(quote)

    }
    fun deleteReminder(reminder: Reminder ) = viewModelScope.launch(Dispatchers.IO) {
        FirebaseUserService.deleteReminder(reminder)

    }
    fun updateReminderState(reminder: Reminder ) = viewModelScope.launch {
        FirebaseUserService.updateReminderState(reminder)

    }

    fun addReminder(reminder: Reminder) = viewModelScope.launch(Dispatchers.IO) {
        FirebaseUserService.addReminder(reminder)

    }

}