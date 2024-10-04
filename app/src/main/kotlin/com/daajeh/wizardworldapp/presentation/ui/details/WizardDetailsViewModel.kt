package com.daajeh.wizardworldapp.presentation.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daajeh.wizardworldapp.domain.WizardRepository
import com.daajeh.wizardworldapp.domain.entity.Wizard
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WizardDetailsViewModel(
    private val wizardRepository: WizardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val wizardId = savedStateHandle.get<String>("wizardId").toString()

    val wizard: StateFlow<Wizard?> =
        wizardRepository
            .getWizardById(wizardId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )


    fun toggleWizardFavouriteState() {
        viewModelScope.launch {
            wizard.value?.let { wizard ->
                wizardRepository
                    .toggleFavorite(wizard.id)
            }
        }
    }
}