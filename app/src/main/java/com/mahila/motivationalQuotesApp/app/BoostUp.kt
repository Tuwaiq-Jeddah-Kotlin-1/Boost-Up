package com.mahila.motivationalQuotesApp.app

import android.app.Application
import android.content.Context
import java.time.Instant


class BoostUp : Application() {

    override fun onCreate() {
        super.onCreate()
        instant = this
    }

    companion object{
        lateinit var instant:Application
        private set
    }
}