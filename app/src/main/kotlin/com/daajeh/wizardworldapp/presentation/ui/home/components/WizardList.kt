package com.daajeh.wizardworldapp.presentation.ui.home.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.daajeh.wizardworldapp.domain.entity.Wizard

@Composable
fun WizardList(wizards: List<Wizard>, onItemClick: (String) -> Unit) {
    LazyColumn {
        item {

        }
        items(wizards) { wizard ->
            WizardListItem(wizard = wizard, onClick = onItemClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WizardListPreview() {
    val sampleWizards = listOf(
        Wizard(id = "1", firstName = "Healing Elixir", lastName = "", elixirs = listOf()),
        Wizard(id = "2", firstName = "Power Elixir", lastName = "", elixirs = listOf())
    )

    WizardList(wizards = sampleWizards, onItemClick = {})
}
