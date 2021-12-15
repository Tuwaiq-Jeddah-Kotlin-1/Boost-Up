package com.mahila.motivationalQuotesApp.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.model.repo.FirebaseUserService
import com.mahila.motivationalQuotesApp.viewModel.UserViewModel


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


