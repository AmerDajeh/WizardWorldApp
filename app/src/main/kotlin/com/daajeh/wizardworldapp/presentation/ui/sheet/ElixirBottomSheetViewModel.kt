package com.daajeh.wizardworldapp.presentation.ui.sheet

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daajeh.wizardworldapp.data.network.NetworkStatusProvider
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.entity.Elixir
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ElixirBottomSheetViewModel(
    private val elixirRepository: ElixirRepository,
    private val networkStatusProvider: NetworkStatusProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val error = MutableStateFlow<String?>(null)

    private val elixirId = savedStateHandle.get<String>("elixirId").toString()

    val elixir: StateFlow<Elixir?> =
        elixirRepository.getElixirById(elixirId)
            .onEach { fetchedElixir ->
                // Check sheet data only if there's no elixir yet and the ID is valid
                fetchedElixir ?: launchElixirCheckingJob()
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null
            )

    private fun launchElixirCheckingJob() {
        viewModelScope.launch {
            if (elixirId.isNotBlank()) {
                elixir.value ?: networkStatusProvider.isNetworkAvailable().let { available ->
                    delay(2_00)
                    if (!available) {
                        // Only update error message if elixir is still null and no network
                        error.value =
                            "Device is offline. Please try again when you have a connection."
                    }
                }
            }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "Elixir ViewModel cleard: ")
        super.onCleared()
    }

    fun toggleElixirFavouriteState() {
        viewModelScope.launch {
            elixir.value?.let { elixir ->
                elixirRepository
                    .toggleFavourite(elixir.id)
            }
        }
    }
}