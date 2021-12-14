package com.mahila.motivationalQuotesApp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.mahila.motivationalQuotesApp.R

class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.auth_fragment, SignupFragment())
            commit()
        }


    }




}