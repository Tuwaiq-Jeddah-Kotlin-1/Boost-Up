package com.mahila.motivationalQuotesApp.model.repository

import android.content.Context
import android.content.SharedPreferences
import com.mahila.motivationalQuotesApp.util.LangUtil

class Storage (context: Context) {
    //-----------------------------LA
    private var preferences: SharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE)

    fun getPreferredLocale(): String {
        return preferences.getString("preferred_locale", LangUtil.OPTION_PHONE_LANGUAGE)!!
    }

    fun setPreferredLocale(localeCode: String) {
        preferences.edit().putString("preferred_locale", localeCode).apply()
    }
}