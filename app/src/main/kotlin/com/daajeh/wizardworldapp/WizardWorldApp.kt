package com.daajeh.wizardworldapp

import android.app.Application
import com.daajeh.wizardworldapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup.onKoinStartup

class WizardWorldApp: Application() {

    init {
        @Suppress("OPT_IN_USAGE")
        onKoinStartup {
            androidLogger()
            androidContext(this@WizardWorldApp)
            modules(appModule)
        }
    }
}