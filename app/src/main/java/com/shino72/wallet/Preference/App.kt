package com.shino72.wallet.Preference

import android.app.Application

class App :Application(){
    companion object {
        lateinit var prefs : AlarmPreference
    }

    override fun onCreate() {
        prefs = AlarmPreference(applicationContext)
        super.onCreate()
    }
}