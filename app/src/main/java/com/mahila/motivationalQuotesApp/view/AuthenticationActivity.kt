package com.mahila.motivationalQuotesApp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.mahila.motivationalQuotesApp.R

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        //----
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_auth_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    override fun onBackPressed() {

        super.onBackPressed()
    }


}