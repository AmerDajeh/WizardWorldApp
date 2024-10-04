package com.daajeh.wizardworldapp.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daajeh.wizardworldapp.domain.entity.Wizard
import com.daajeh.wizardworldapp.presentation.ui.home.components.WizardList

@Composable
fun WizardsScreen(
    wizards: List<Wizard>,
    error: String?,
    details: (String) -> Unit
) {

    when {
        error != null ->
            Error(error)

        wizards.isEmpty() ->
            Loading()

        else ->
            WizardsContent(
                wizards = wizards,
                onElixirClick = details
            )
    }
}

@Composable
private fun Error(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            text = message
        )
    }
}

@Composable
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
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