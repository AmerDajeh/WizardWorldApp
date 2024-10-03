package com.daajeh.wizardworldapp.presentation.ui.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.Ingredient
import com.daajeh.wizardworldapp.domain.entity.Inventor

@Composable
fun ElixirSheet(elixir: Elixir, onFavoriteToggle: () -> Unit) {
    var isFavorite by remember(elixir.isFavorite) { mutableStateOf(elixir.isFavorite) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Elixir Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = elixir.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            // Favorite Icon
            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    onFavoriteToggle()
                }
            ) {
                Icon(
                    painter = if (isFavorite) painterResource(id = android.R.drawable.btn_star_big_on)
                    else painterResource(id = android.R.drawable.btn_star_big_off),
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Yellow else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Elixir Details
        ElixirDetailItem(label = "Characteristics", value = elixir.characteristics)
        ElixirDetailItem(label = "Difficulty", value = elixir.difficulty)
        ElixirDetailItem(label = "Effect", value = elixir.effect)
        ElixirDetailItem(label = "Side Effects", value = elixir.sideEffects)
        ElixirDetailItem(label = "Brewing Time", value = elixir.time)
        ElixirDetailItem(label = "Manufacturer", value = elixir.manufacturer)

        Spacer(modifier = Modifier.height(16.dp))

        // Ingredients list
        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (elixir.ingredients.isNotEmpty())
            LazyColumn {
                items(elixir.ingredients) { ingredient ->
                    ElixirDetailItem(label = ingredient.name, value = "")
                }
            }
        else
            Text(
                text = "None",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

        Spacer(modifier = Modifier.height(16.dp))

        // Inventors list
        Text(
            text = "Inventors",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (elixir.inventors.isNotEmpty())
            LazyColumn {
                items(elixir.inventors) { inventor ->
                    ElixirDetailItem(label = inventor.firstName, value = "")
                }
            }
        else
            Text(
                text = "None",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
    }
}

@Composable
fun ElixirDetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        val caption = remember {
            value.ifBlank { "None" }
        }
        Text(
            text = caption,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewElixirScreen() {
    ElixirSheet(
        elixir =
        Elixir(
            characteristics = "Translucent, shiny liquid",
            difficulty = "Hard",
            effect = "Grants temporary invisibility",
            ingredients = listOf(Ingredient("Bat wings"), Ingredient("Fairy dust")),
            inventors = listOf(Inventor("Severus Snape")),
            manufacturer = "Ministry of Magic",
            name = "Invisibility Potion",
            sideEffects = "Dizziness",
            time = "12 hours brewing",
            isFavorite = true
        )
    ) {
        // Handle favorite toggle action
    }
}
