package com.mahila.motivationalQuotesApp.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mahila.motivationalQuotesApp.model.entities.Notification
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.entities.User
import com.mahila.motivationalQuotesApp.model.repository.FirebaseUserService
import com.mahila.motivationalQuotesApp.model.repository.FirebaseUserService.firebaseUserService
import kotlinx.coroutines.launch


class UserViewModel(application: Application) : AndroidViewModel(application)//: ViewModel()
{
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _FavoritesQuotes = MutableLiveData<List<Quote>>()
    val favoritesQuotes: LiveData<List<Quote>> = _FavoritesQuotes
    private val _Notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _Notifications
  //  var userState = firebaseUserMutableLiveData

    init {
        firebaseUserService(application)
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

    fun addNotification(notification: Notification) = viewModelScope.launch {
        FirebaseUserService.addNotification(notification)

    }

}