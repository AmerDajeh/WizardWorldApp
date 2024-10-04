package com.daajeh.wizardworldapp.presentation.ui.home

import com.daajeh.wizardworldapp.domain.entity.Wizard

sealed class WizardsUiState {
    object Loading : WizardsUiState()
    data class Success(val wizards: List<Wizard>) : WizardsUiState()
    data class Error(val message: String) : WizardsUiState()
}
