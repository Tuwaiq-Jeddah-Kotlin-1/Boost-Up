package com.mahila.motivationalQuotesApp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahila.motivationalQuotesApp.model.repo.FirebaseUserService
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    fun signUp(name:String,email:String,password:String) = viewModelScope.launch {
        FirebaseUserService.signUp(name, email,password)
    }

    fun signIn( email: String, password: String) = viewModelScope.launch {
        FirebaseUserService.signIn( email, password)
    }

    fun signOut( ) = viewModelScope.launch {
        FirebaseUserService.signOut()
    }
    fun forgotPassword(email: String)= viewModelScope.launch {
        FirebaseUserService.forgotPassword(email)

    }
}