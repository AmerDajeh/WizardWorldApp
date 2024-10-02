package com.daajeh.wizardworldapp.presentation.ui.home

import androidx.compose.runtime.Composable
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.presentation.ui.components.ElixirList

@Composable
fun ElixirsScreen(
    elixirs: List<Elixir>,
    details: (String) -> Unit
) {

    ElixirsContent(
        elixirs = elixirs,
        onElixirClick = details
    )
}

@Composable
private fun ElixirsContent(
    elixirs: List<Elixir>,
    onElixirClick: (String) -> Unit
) {
    ElixirList(
        elixirs = elixirs,
        onItemClick = onElixirClick
    )
}