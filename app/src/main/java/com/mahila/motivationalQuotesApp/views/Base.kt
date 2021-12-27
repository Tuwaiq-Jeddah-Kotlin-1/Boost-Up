package com.mahila.motivationalQuotesApp.views

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mahila.motivationalQuotesApp.app.MQApp
import com.mahila.motivationalQuotesApp.model.repository.Storage
import com.mahila.motivationalQuotesApp.util.LangUtil

open class Base : AppCompatActivity() {
    private lateinit var oldPrefLocaleCode : String
    protected val storage : Storage by lazy {
        (application as MQApp).storage
    }

    private fun resetTitle() {
        try {
            val label = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA).labelRes;
            if (label != 0) {
                setTitle(label);
            }
        } catch (e: PackageManager.NameNotFoundException) {

        }
    }

    override fun attachBaseContext(newBase: Context) {
        val currentLocaleCode = Storage(newBase).getPreferredLocale()
        val prefLocale = LangUtil.getLocaleFromPrefCode(currentLocaleCode)
        val localeUpdatedContext: ContextWrapper = LangUtil.updateContextLocale(newBase, prefLocale)
        oldPrefLocaleCode = currentLocaleCode
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MQApp).invalidateConfiguration()
        resetTitle()
    }

    override fun onResume() {
        val currentLocaleCode = Storage(this).getPreferredLocale()
        if(oldPrefLocaleCode != currentLocaleCode){
            (application as MQApp).invalidateConfiguration()
            recreate()
            oldPrefLocaleCode = currentLocaleCode
        }
        super.onResume()
    }
}