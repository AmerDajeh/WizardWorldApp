package com.daajeh.wizardworldapp.presentation.ui.details

import android.R.drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daajeh.wizardworldapp.domain.entity.LightElixir
import com.daajeh.wizardworldapp.domain.entity.Wizard

@Composable
fun WizardDetailsScreen(
    wizard: Wizard?,
    onToggleFavourite: () -> Unit,
    onBackClick: () -> Unit
) {

    wizard?.let{
        var isFavorite by remember(wizard.isFavorite) { mutableStateOf(wizard.isFavorite) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
                IconButton(onClick = {
                    isFavorite = !isFavorite
                    onToggleFavourite()
                }) {
                    Icon(
                        painter = if (isFavorite) painterResource(id = drawable.btn_star_big_on)
                        else painterResource(id = drawable.btn_star_big_off),
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Yellow else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Elixirs list
            Text(
                text = "Elixirs",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(wizard.elixirs) { elixir ->
                    ElixirItem(elixir)
                }
            }
        }
    } ?: WizardNotFound(onBackClick = onBackClick)
}

@Composable
fun ElixirItem(elixir: LightElixir) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Navigate to elixir details */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = elixir.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWizardScreen() {
    // Sample data for preview
    val sampleWizard = Wizard(
        elixirs = listOf(
            LightElixir("Elixir of Life"),
            LightElixir("Polyjuice Potion"),
            LightElixir("Felix Felicis")
        ),
        firstName = "Harry",
        id = "1",
        lastName = "Potter",
        isFavorite = true
    )

    WizardDetailsScreen(wizard = sampleWizard, {}) {
        // Handle favorite toggle action
    }
}

@Composable
private fun WizardNotFound(
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
                text = "Wizard Not Found",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp)) // Space between text and button
            Button(onClick = onBackClick) {
                Text(text = "Go Back")
            }
        }
    }
}