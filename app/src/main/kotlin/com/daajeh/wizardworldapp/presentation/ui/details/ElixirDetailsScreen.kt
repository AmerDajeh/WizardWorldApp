package com.daajeh.wizardworldapp.presentation.ui.details

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.Ingredient
import com.daajeh.wizardworldapp.domain.entity.Inventor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElixirDetailsScreen(elixir: Elixir?) {

    LaunchedEffect(elixir){
        elixir?.let { Log.d(TAG, "ElixirDetailsScreen: $it") }
    }
    when {
        elixir != null ->
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = elixir.name) }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Manufacturer: ${elixir.manufacturer}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Difficulty: ${elixir.difficulty}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Effect: ${elixir.effect}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Time: ${elixir.time}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Characteristics",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = elixir.characteristics,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ingredients",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    elixir.ingredients.forEach { ingredient ->
                        Text(
                            text = "• ${ingredient.name}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Inventors",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    elixir.inventors.forEach { inventor ->
                        Text(
                            text = "${inventor.firstName} ${inventor.lastName}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Side Effects",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = elixir.sideEffects,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }


        else ->
            ElixirNotFound(
                modifier = Modifier
            )
    }

}


@Preview(showBackground = true)
@Composable
fun ElixirDetailsPreview() {
    val sampleElixir = Elixir(
        characteristics = "Restores health and boosts energy",
        difficulty = "Medium",
        effect = "Healing and energy boost",
        id = "1",
        ingredients = listOf(
            Ingredient(id = "1", name = "Herb"),
            Ingredient(id = "2", name = "Magic Water")
        ),
        inventors = listOf(
            Inventor(firstName = "John", lastName = "Doe", id = "1"),
            Inventor(firstName = "Jane", lastName = "Smith", id = "2")
        ),
        manufacturer = "Herb Co.",
        name = "Healing Elixir",
        sideEffects = "Mild nausea",
        time = "5 minutes"
    )

    ElixirDetailsScreen(elixir = sampleElixir)
}


@Composable
private fun ElixirNotFound(
    modifier: Modifier = Modifier
){
    // todo
}