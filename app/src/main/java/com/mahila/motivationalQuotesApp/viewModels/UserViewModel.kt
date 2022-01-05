package com.mahila.motivationalQuotesApp.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.mahila.motivationalQuotesApp.model.entities.Reminder
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.entities.User
import com.mahila.motivationalQuotesApp.model.repository.FirebaseUserService
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
            _Notifications.value = FirebaseUserService.getNotifications()
        }

    }

    fun signUp(name: String, email: String, password: String) = viewModelScope.launch {
        FirebaseUserService.signUp(name, email, password)
    }

    fun signIn(email: String, password: String,view: View) =
        viewModelScope.launch { FirebaseUserService.signIn(email, password,view) }

    fun signOut() = viewModelScope.launch {
        FirebaseUserService.signOut()
    }

    fun forgotPassword(email: String) = viewModelScope.launch {
        FirebaseUserService.forgotPassword(email)

    }

    fun checkSignInState() = FirebaseUserService.checkSignInState()

    fun resetUserName(newUserName: String) = viewModelScope.launch {
        FirebaseUserService.resetUserName(newUserName)

    }

    fun addFavoriteQuote(quote: Quote) = viewModelScope.launch {
        FirebaseUserService.addFavoriteQuote(quote)

    }
    fun deleteReminder(reminder: Reminder ) = viewModelScope.launch {
        FirebaseUserService.deleteReminder(reminder)

    }

    fun addReminder(reminder: Reminder) = viewModelScope.launch {
        FirebaseUserService.addNotification(reminder)

    }

}