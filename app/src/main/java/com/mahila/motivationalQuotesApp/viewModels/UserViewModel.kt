package com.mahila.motivationalQuotesApp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahila.motivationalQuotesApp.model.entities.Notification
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.entities.User
import com.mahila.motivationalQuotesApp.model.repository.FirebaseUserService
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _FavoritesQuotes = MutableLiveData<List<Quote>>()
    val favoritesQuotes: LiveData<List<Quote>> = _FavoritesQuotes
    private val _Notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _Notifications

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

    fun signIn(email: String, password: String) = viewModelScope.launch {
        FirebaseUserService.signIn(email, password)
    }

    fun signOut() = viewModelScope.launch {
        FirebaseUserService.signOut()
    }

    fun forgotPassword(email: String) = viewModelScope.launch {
        FirebaseUserService.forgotPassword(email)

    }

    fun checksignInState() = FirebaseUserService.checkSignInState()

    fun resetUserName(newUserName: String) = viewModelScope.launch {
        FirebaseUserService.resetUserName(newUserName)

    }

    fun resetPassword(newPassword: String) = viewModelScope.launch {
        FirebaseUserService.resetPassword(newPassword)

    }

    fun addFavoriteQuote(quote: Quote) = viewModelScope.launch {
        FirebaseUserService.addFavoriteQuote(quote)

    }

    fun addNotification(notification: Notification) = viewModelScope.launch {
        FirebaseUserService.addNotification(notification)

    }

}