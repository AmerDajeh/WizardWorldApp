package com.daajeh.wizardworldapp.presentation.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.daajeh.wizardworldapp.domain.entity.LightElixir

@Composable
fun ElixirList(elixirs: List<LightElixir>, onItemClick: (String) -> Unit) {
    LazyColumn {
        items(elixirs) { elixir ->
            ElixirListItem(elixir = elixir, onClick = onItemClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ElixirListPreview() {
    val sampleElixirs = listOf(
        LightElixir(id = "1", name = "Healing Elixir", difficulty = "Medium"),
        LightElixir(id = "2", name = "Power Elixir", difficulty = "Hard")
    )

    ElixirList(elixirs = sampleElixirs, onItemClick = {})
}
