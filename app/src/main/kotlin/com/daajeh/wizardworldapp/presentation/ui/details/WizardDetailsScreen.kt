package com.daajeh.wizardworldapp.presentation.ui.details

import android.R.drawable
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daajeh.wizardworldapp.R
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.LightElixir
import com.daajeh.wizardworldapp.domain.entity.Wizard
import com.daajeh.wizardworldapp.presentation.ui.details.components.ElixirSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WizardDetailsScreen(
    wizard: Wizard?,
    elixir: Elixir?,
    onLoadElixir: (String) -> Unit,
    onBottomSheetClosed: () -> Unit = {},
    onToggleFavouriteWizard: () -> Unit,
    onToggleFavouriteElixir: () -> Unit,
    onBackClick: () -> Unit
) {

    BackHandler {
        onBackClick()
    }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    val bottomSheetState =
        rememberModalBottomSheetState()

    wizard?.let {
        WizardDetails(
            wizard = it,
            onToggleFavouriteWizard = onToggleFavouriteWizard,
            onLoadElixir = { elixirId ->
                onLoadElixir(elixirId)
                openBottomSheet = true
            }
        )
    } ?: ItemNotFound(onBackClick = onBackClick)

    // Sheet content
    if (openBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.statusBarsPadding(),
            onDismissRequest = {
                openBottomSheet = false
                onBottomSheetClosed()
            },
            sheetState = bottomSheetState,
        ) {
            elixir?.let {
                ElixirSheet(
                    elixir = elixir,
                    onFavoriteToggle = onToggleFavouriteElixir
                )
            } ?: ItemNotFound(onBackClick = {openBottomSheet = false})
        }
    }
}

@Composable
private fun ItemNotFound(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit // Callback for back button click
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.item_not_found),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp)) // Space between text and button
            Button(onClick = onBackClick) {
                Text(text = stringResource(R.string.go_back))
            }
        }
    }
}

@Composable
fun WizardDetails(
    wizard: Wizard,
    onToggleFavouriteWizard: () -> Unit,
    onLoadElixir: (String) -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }
    var openBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Full Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${wizard.firstName} ${wizard.lastName}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            // Favorite Icon
            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    onToggleFavouriteWizard()
                }
            ) {
                Icon(
                    painter = if (isFavorite) painterResource(id = drawable.btn_star_big_on)
                    else painterResource(id = drawable.btn_star_big_off),
                    contentDescription = stringResource(R.string.favorite),
                    tint = if (isFavorite) Color.Yellow else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Traits Section
        Text(
            text = stringResource(R.string.traits),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(wizard.traits) { trait ->
                TraitChip(trait)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Elixirs list
        Text(
            text = stringResource(R.string.elixirs),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLow, MaterialTheme.shapes.small)
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            items(wizard.elixirs) { elixir ->
                ElixirItem(elixir) {
                    openBottomSheet = true
                    onLoadElixir(elixir.id)
                }
            }
        }
    }
}

@Composable
fun TraitChip(trait: String) {
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = trait,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun ElixirItem(elixir: LightElixir, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Add content for the Elixir item
            Text(
                text = elixir.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// Preview Function
@Preview(showBackground = true)
@Composable
fun WizardDetailsPreview() {
    // Sample wizard data
    val sampleWizard = Wizard(
        firstName = "Harry",
        lastName = "Potter",
        traits = listOf("Bravery", "Courage", "Determination"),
        elixirs = listOf(
            LightElixir(id = "1", name = "Polyjuice Potion"),
            LightElixir(id = "2", name = "Felix Felicis"),
            LightElixir(id = "3", name = "Amortentia")
        )
    )

    WizardDetailsScreen(
        wizard = sampleWizard,
        onToggleFavouriteWizard = {},
        onLoadElixir = {},
        elixir = null,
        onBottomSheetClosed = {  },
        onToggleFavouriteElixir = { },
        onBackClick = {  },
    )
}
