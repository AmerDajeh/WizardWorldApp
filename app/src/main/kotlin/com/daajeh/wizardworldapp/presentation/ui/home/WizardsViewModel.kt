package com.daajeh.wizardworldapp.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daajeh.wizardworldapp.data.network.NetworkStatusProvider
import com.daajeh.wizardworldapp.domain.WizardRepository
import com.daajeh.wizardworldapp.domain.entity.Wizard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class WizardsViewModel(
    private val networkStatusProvider: NetworkStatusProvider,
    wizardRepository: WizardRepository
) : ViewModel() {

    val error = MutableStateFlow<String?>(null)

    val wizards: StateFlow<List<Wizard>> =
        wizardRepository
            .getWizards()
            .onEach { fetchedWizards ->
                if (fetchedWizards.isEmpty()) {
                    if (!networkStatusProvider.isNetworkAvailable())
                        error.update { "" }
                }
            }
            .catch { exception -> error.update { exception.message ?: "something went wrong." } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                listOf()
            )
}