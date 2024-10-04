package com.daajeh.wizardworldapp.di

import android.content.Context
import androidx.room.Room
import com.daajeh.wizardworldapp.data.ElixirRepositoryImpl
import com.daajeh.wizardworldapp.data.WizardRepositoryImpl
import com.daajeh.wizardworldapp.data.local.WizardWorldDatabase
import com.daajeh.wizardworldapp.data.network.NetworkStatusProvider
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.WizardRepository
import com.daajeh.wizardworldapp.presentation.ui.home.WizardsViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { provideDatabase(get()) }

    viewModelOf(::WizardsViewModel)

    scope<WizardsViewModel> {
        scopedOf(::NetworkStatusProvider)

        scoped {
            val database: WizardWorldDatabase = get()
            database.wizardDao()
        }

        scoped {
            val database: WizardWorldDatabase = get()
            database.elixirDao()
        }

        scoped {
            val database: WizardWorldDatabase = get()
            database.ingredientDao()
        }

        scoped {
            val database: WizardWorldDatabase = get()
            database.inventorDao()
        }

        scopedOf(::WizardRepositoryImpl) {
            bind<WizardRepository>()
        }

        scopedOf(::ElixirRepositoryImpl) {
            bind<ElixirRepository>()
        }
    }
}


fun provideDatabase(context: Context): WizardWorldDatabase {
    return Room.databaseBuilder(context, WizardWorldDatabase::class.java, "wizard").build()
}