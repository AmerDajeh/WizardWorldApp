package com.daajeh.wizardworldapp.presentation.ui.home

import androidx.compose.runtime.Composable
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.LightElixir
import com.daajeh.wizardworldapp.presentation.ui.components.ElixirList

@Composable
fun ElixirsScreen(
    elixirs: List<LightElixir>,
    details: (String) -> Unit
) {

    ElixirsContent(
        elixirs = elixirs,
        onElixirClick = details
    )
}

@Composable
private fun ElixirsContent(
    elixirs: List<LightElixir>,
    onElixirClick: (String) -> Unit
) {
    ElixirList(
        elixirs = elixirs,
        onItemClick = onElixirClick
    )
}