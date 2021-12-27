package com.mahila.motivationalQuotesApp.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.ActivityMainBinding

lateinit var sharedDarkModeFlag: SharedPreferences
var sharePreferencesValue: String? = null
const val SHARED_KEY = "DARK_MODE"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedDarkModeFlag = this.getSharedPreferences("sharedDarkModeFlag", Context.MODE_PRIVATE)
        sharePreferencesValue = sharedDarkModeFlag.getString(SHARED_KEY, "Auto")
        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_quotes,
                R.id.navigation_favorite,
                R.id.navigation_notification,
                R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_signin -> {
                    hideBoth()
                }
                R.id.navigation_signup -> {
                    hideBoth()
                }
                R.id.navigation_forgotPassword -> {
                    hideOne()
                }
                R.id.navigation_addNotification -> {
                    hideOne()
                }
                R.id.navigation_openNotificationQuote -> {
                    hideOne()
                }
                else -> {
                    supportActionBar?.show()
                    binding.navView.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun hideBoth() {
        supportActionBar?.hide()
        binding.navView.visibility = View.GONE
    }

    private fun hideOne() {
        supportActionBar?.show()
        binding.navView.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}