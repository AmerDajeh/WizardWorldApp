package com.daajeh.wizardworldapp.di

import androidx.room.Room
import com.daajeh.wizardworldapp.data.ElixirRepositoryImpl
import com.daajeh.wizardworldapp.data.WizardRepositoryImpl
import com.daajeh.wizardworldapp.data.local.WizardWorldDatabase
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.WizardRepository
import com.daajeh.wizardworldapp.work.FetchWizardDataWorker
import com.daajeh.wizardworldapp.work.UpdateDataWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module

val workModule = module {

    workerOf(::UpdateDataWorker)
    workerOf(::FetchWizardDataWorker)

    scope<UpdateDataWorker> {
        scoped { Room.databaseBuilder(get(), WizardWorldDatabase::class.java, "wizard").build() }

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

    scope<FetchWizardDataWorker> {
        scoped { Room.databaseBuilder(get(), WizardWorldDatabase::class.java, "wizard").build() }

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