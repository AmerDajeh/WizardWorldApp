package com.daajeh.wizardworldapp.presentation.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.daajeh.wizardworldapp.data.network.NetworkStatusProvider
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.WizardRepository
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.Wizard
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.scope.ScopeViewModel

class WizardsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ScopeViewModel() {
    private val wizardRepository by scope.inject<WizardRepository>()
    private val elixirRepository by scope.inject<ElixirRepository>()
    private val networkStatusProvider by scope.inject<NetworkStatusProvider>()

    val error = MutableStateFlow<String?>(null)

    val wizards: StateFlow<List<Wizard>> =
        wizardRepository
            .getWizards()
            .onStart {
                checkNetworkBeforeFetchingData()
            }
            .onEach { checkIfCachedWizardsAvailable(it) }
            .catch { exception ->  error.update { exception.message ?: "something went wrong." }}
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                listOf()
            )

    private fun checkIfCachedWizardsAvailable(wizards: List<Wizard>) {
        viewModelScope.launch {
            error.value?.let { if (wizards.isNotEmpty()) error.update { null } }
        }
    }

    private fun checkNetworkBeforeFetchingData() {
        viewModelScope.launch {
            if (!networkStatusProvider.isNetworkAvailable())
                error.update { "device is offline, try again when you have connection." }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val wizard: StateFlow<Wizard?> =
        savedStateHandle.getStateFlow("wizard_id", "")
            .flatMapLatest {
                if (it.isNotBlank())
                    wizardRepository
                        .getWizardById(it)
                else
                    flowOf(null)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val elixir: StateFlow<Elixir?> =
        savedStateHandle.getStateFlow("elixir_id", "")
            .flatMapLatest {
                if (it.isNotBlank())
                    elixirRepository
                        .getElixirById(it)
                else
                    flowOf(null)
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )


    fun load(wizardId: String) {
        viewModelScope.launch {
            savedStateHandle["wizard_id"] = wizardId
        }
    }

    fun loadElixir(elixirId: String) {
        viewModelScope.launch {
            savedStateHandle["elixir_id"] = elixirId
        }
    }

    fun toggleWizardFavouriteState() {
        viewModelScope.launch {
            wizard.value?.let { wizard ->
                when {
                    wizard.isFavorite ->
                        wizardRepository.removeFavouriteWizard(wizard.id)

                    else ->
                        wizardRepository
                            .saveFavouriteWizard(wizard.id)
                }
            }
        }
    }

    fun toggleElixirFavouriteState() {
        viewModelScope.launch {
            elixir.value?.let { elixir ->
                when {
                    elixir.isFavorite ->
                        elixirRepository.removeFavouriteElixir(elixir.id)

                    else ->
                        elixirRepository
                            .saveFavouriteElixir(elixir.id)
                }
            }
        }
    }
}