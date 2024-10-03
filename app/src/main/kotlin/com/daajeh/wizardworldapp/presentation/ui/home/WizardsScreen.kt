package com.daajeh.wizardworldapp.presentation.ui.home

import androidx.compose.runtime.Composable
import com.daajeh.wizardworldapp.domain.entity.Wizard
import com.daajeh.wizardworldapp.presentation.ui.home.components.WizardList

@Composable
fun WizardsScreen(
    wizards: List<Wizard>,
    details: (String) -> Unit
) {

    WizardsContent(
        wizards = wizards,
        onElixirClick = details
    )
}

@Composable
private fun WizardsContent(
    wizards: List<Wizard>,
    onElixirClick: (String) -> Unit
) {
    WizardList(
        wizards = wizards,
        onItemClick = onElixirClick
    )
}