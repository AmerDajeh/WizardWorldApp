package com.daajeh.wizardworldapp.presentation.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.daajeh.wizardworldapp.domain.entity.Elixir

@Composable
fun ElixirList(elixirs: List<Elixir>, onItemClick: (String) -> Unit) {
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
        Elixir(id = "1", name = "Healing Elixir", manufacturer = "Herb Co.", difficulty = "Medium"),
        Elixir(id = "2", name = "Power Elixir", manufacturer = "Potion Labs", difficulty = "Hard")
    )

    ElixirList(elixirs = sampleElixirs, onItemClick = {})
}
