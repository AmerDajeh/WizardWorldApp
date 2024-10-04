package com.daajeh.wizardworldapp.di

import com.daajeh.wizardworldapp.data.ElixirRepositoryImpl
import com.daajeh.wizardworldapp.data.WizardRepositoryImpl
import com.daajeh.wizardworldapp.data.local.WizardWorldDatabase
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.WizardRepository
import com.daajeh.wizardworldapp.work.FetchElixirDataWorker
import com.daajeh.wizardworldapp.work.FetchWizardsWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module

val workModule = module {

    workerOf(::FetchWizardsWorker)
    workerOf(::FetchElixirDataWorker)

    scope<FetchWizardsWorker> {
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

    scope<FetchElixirDataWorker> {
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
