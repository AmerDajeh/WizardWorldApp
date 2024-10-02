package com.daajeh.wizardworldapp.presentation.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.LightElixir
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.androidx.scope.ScopeViewModel

class ElixirsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ScopeViewModel() {
    private val repository by scope.inject<ElixirRepository>()
    val elixirs: StateFlow<List<LightElixir>> =
        repository
            .getElixirs()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                listOf()
            )


    @OptIn(ExperimentalCoroutinesApi::class)
    val elixir: StateFlow<Elixir?> =
        savedStateHandle.getStateFlow("elixir_id", "")
            .flatMapLatest {
                repository
                    .getElixirById(it)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    fun load(elixirId: String){
        viewModelScope.launch {
            savedStateHandle["elixir_id"] = elixirId
        }
    }

    fun toggleElixirFavouriteState(elixirId: String) {
        viewModelScope.launch {
            // todo
        }
    }
}