package com.daajeh.wizardworldapp.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daajeh.wizardworldapp.R
import com.daajeh.wizardworldapp.domain.entity.Wizard
import com.daajeh.wizardworldapp.presentation.ui.home.components.WizardList
import kotlinx.coroutines.delay

@Composable
fun WizardsScreen(
    wizards: List<Wizard>,
    error: String?,
    details: (String) -> Unit
) {
    var showError by remember { mutableStateOf(false) }

    when {
        showError && error != null ->
            DeviceOfflineError(modifier = Modifier.fillMaxSize())

        wizards.isEmpty() ->
            Loading()

        else ->
            WizardsContent(
                wizards = wizards,
                onElixirClick = details
            )
    }
    LaunchedEffect(wizards){
        delay(3_000)
        showError = wizards.isEmpty() && error != null
    }
}

@Composable
fun DeviceOfflineError(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            text = stringResource(R.string.device_is_offline_try_again_when_you_have_connection)
        )
    }
}

@Composable
fun Loading() {
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