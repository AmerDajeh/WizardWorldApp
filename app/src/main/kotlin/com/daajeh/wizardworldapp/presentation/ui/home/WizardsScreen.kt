package com.daajeh.wizardworldapp.presentation.ui.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.WizardsScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    wizards: List<Wizard>,
    error: String?,
    details: (String) -> Unit
) {
    when {
        wizards.isNotEmpty() ->
            WizardsContent(
                animatedVisibilityScope = animatedVisibilityScope,
                wizards = wizards,
                onElixirClick = details
            )

        error != null ->
            DeviceOfflineError(modifier = Modifier.fillMaxSize())

        else ->
            Loading()
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.WizardsContent(
    animatedVisibilityScope: AnimatedVisibilityScope,
    wizards: List<Wizard>,
    onElixirClick: (String) -> Unit
) {
    WizardList(
        animatedVisibilityScope = animatedVisibilityScope,
        wizards = wizards,
        onItemClick = onElixirClick
    )
}