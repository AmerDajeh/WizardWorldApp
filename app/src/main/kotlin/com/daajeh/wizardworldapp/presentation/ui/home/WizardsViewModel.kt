package com.daajeh.wizardworldapp.presentation.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.entity.Wizard
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.androidx.scope.ScopeViewModel

class WizardsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ScopeViewModel() {
    private val repository by scope.inject<ElixirRepository>()
    val wizards: StateFlow<List<Wizard>> =
        repository
            .getWizards()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                listOf()
            )


    @OptIn(ExperimentalCoroutinesApi::class)
    val wizard: StateFlow<Wizard?> =
        savedStateHandle.getStateFlow("wizard_id", "")
            .flatMapLatest {
                repository
                    .getWizardById(it)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    fun load(wizardId: String) {
        viewModelScope.launch {
            savedStateHandle["wizard_id"] = wizardId
        }
    }

    fun toggleWizardFavouriteState() {
        viewModelScope.launch {
            wizard.value?.let { wizard ->
                when {
                    wizard.isFavorite ->
                        repository.removeFavouriteWizard(wizard.id)

                    else ->
                        repository
                            .saveFavouriteWizard(wizard.id)
                }
            }
        }
    }
}