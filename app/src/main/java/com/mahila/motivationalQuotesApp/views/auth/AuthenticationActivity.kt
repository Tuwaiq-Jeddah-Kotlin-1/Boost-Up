package com.mahila.motivationalQuotesApp.views.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.viewModels.UserViewModel
import com.mahila.motivationalQuotesApp.views.homeScreen.MainActivity


class AuthenticationActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.auth_fragment, SignupFragment())
            commit()
        }



    }

    public override fun onStart() {
        super.onStart()
        if (userViewModel.checksignInState()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

}


