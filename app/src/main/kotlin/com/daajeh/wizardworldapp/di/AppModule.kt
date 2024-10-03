package com.daajeh.wizardworldapp.di

import androidx.room.Room
import com.daajeh.wizardworldapp.data.ElixirRepositoryImpl
import com.daajeh.wizardworldapp.data.local.WizardWorldDatabase
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.presentation.ui.home.WizardsViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::WizardsViewModel)

    scope<WizardsViewModel> {
        scoped { Room.databaseBuilder(get(), WizardWorldDatabase::class.java, "wizard").build() }

        scopedOf(::ElixirRepositoryImpl) {
            bind<ElixirRepository>()
        }
    }
}