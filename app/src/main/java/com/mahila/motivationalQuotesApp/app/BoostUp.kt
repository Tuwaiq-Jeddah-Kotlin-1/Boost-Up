package com.mahila.motivationalQuotesApp.app

import android.app.Application


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