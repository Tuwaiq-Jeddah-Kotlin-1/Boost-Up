package com.mahila.motivationalQuotesApp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahila.motivationalQuotesApp.model.entity.User
import com.mahila.motivationalQuotesApp.model.repo.FirebaseUserService
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init {
        viewModelScope.launch {
            _user.value = FirebaseUserService.getUserData()
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
    fun resetPassword(newPassword: String) = viewModelScope.launch {
        FirebaseUserService.resetPassword(newPassword)

    }

}