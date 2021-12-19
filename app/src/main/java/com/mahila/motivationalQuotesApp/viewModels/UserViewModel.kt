package com.mahila.motivationalQuotesApp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.entities.User
import com.mahila.motivationalQuotesApp.model.repository.FirebaseUserService
import kotlinx.coroutines.launch

 class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val _FavoritesQuotes = MutableLiveData<List<Quote>>()
    val favoritesQuotes: LiveData<List<Quote>> = _FavoritesQuotes

    init {
        viewModelScope.launch {
            _user.value = FirebaseUserService.getUserData()
        }
        viewModelScope.launch {
            _FavoritesQuotes.value = FirebaseUserService.getFavoritesQuotes()
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

    fun checksignInState() = FirebaseUserService.checksignInState()

 fun resetUserName(newUserName: String) = viewModelScope.launch {
        FirebaseUserService.resetUserName(newUserName)

    }
    fun resetPassword(newPassword: String) = viewModelScope.launch {
        FirebaseUserService.resetPassword(newPassword)

    }

    fun addFavoriteQuote(quote: Quote) = viewModelScope.launch {
        FirebaseUserService.addFavoriteQuote(quote)

    }

}