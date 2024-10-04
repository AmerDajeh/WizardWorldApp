package com.daajeh.wizardworldapp

import android.app.Application
import com.daajeh.wizardworldapp.di.appModule
import com.daajeh.wizardworldapp.di.networkModule
import com.daajeh.wizardworldapp.di.workModule
import com.daajeh.wizardworldapp.work.FetchWizardsWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.androix.startup.KoinStartup.onKoinStartup

class WizardWorldApp: Application() {

    init {
        @Suppress("OPT_IN_USAGE")
        onKoinStartup {
            androidLogger()
            androidContext(this@WizardWorldApp)
            workManagerFactory()
            modules(networkModule, appModule, workModule)
        }
    }

    override fun onCreate() {
        super.onCreate()
        FetchWizardsWorker.enqueue(this)
    }
}