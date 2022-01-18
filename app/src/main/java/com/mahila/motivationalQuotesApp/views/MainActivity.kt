package com.mahila.motivationalQuotesApp.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.databinding.ActivityMainBinding
import java.util.*

lateinit var sharedPre: SharedPreferences
var sharePreferencesValueOfMode: String? = null
var sharePreferencesValueOfLang: String? = null
const val SHARED_MODE_KEY = "MODE"
const val SHARED_LANG_KEY = "LANG"
const val SHARED_STAY_SIGNED_IN = "SIGNED_IN"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPre = this.getSharedPreferences("sharedPre", Context.MODE_PRIVATE)
        sharePreferencesValueOfMode = sharedPre.getString(SHARED_MODE_KEY, "Auto")
        changeMode()
        sharePreferencesValueOfLang = sharedPre.getString(SHARED_LANG_KEY, "Auto")
        sharePreferencesValueOfLang?.let {
            applyLocalized(it)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                    hideBoth()
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

    private fun changeMode() {
        when (sharePreferencesValueOfMode) {
            "LIGHT" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            "DARK" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
            }
        }
    }

    private fun applyLocalized(_langCode: String) {
        //  var langCode = _langCode
        if (_langCode != "Auto") {
            val locale = Locale(_langCode)
            Locale.setDefault(locale)
            val configuration = resources.configuration
            configuration?.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}