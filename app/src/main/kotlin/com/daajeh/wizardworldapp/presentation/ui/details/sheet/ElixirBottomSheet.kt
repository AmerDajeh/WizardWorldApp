package com.daajeh.wizardworldapp.presentation.ui.details.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.Ingredient
import com.daajeh.wizardworldapp.domain.entity.Inventor

@Composable
fun ElixirSheet(elixir: Elixir, onFavoriteToggle: () -> Unit) {
    var isFavorite by remember(elixir.isFavorite) { mutableStateOf(elixir.isFavorite) }

    LazyColumn {
        // Elixir Name
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHighest,
                        RoundedCornerShape(25f)
                    )
                    .padding(8.dp)
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
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Elixir Details
                Characteristics(label = "Characteristics", value = elixir.characteristics)
                ElixirDetailItem(label = "Difficulty", value = elixir.difficulty)
                ElixirDetailItem(label = "Effect", value = elixir.effect)
                ElixirDetailItem(label = "Side Effects", value = elixir.sideEffects)
                ElixirDetailItem(label = "Brewing Time", value = elixir.time)
                ElixirDetailItem(label = "Manufacturer", value = elixir.manufacturer)
            }
        }
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh,
                        RoundedCornerShape(15f)
                    ).padding(8.dp)
            ) {
                // Ingredients list
                Spacer(Modifier.height(8.dp))
                SectionLabel("Ingredients")
                Spacer(Modifier.height(8.dp))
                if (elixir.ingredients.isNotEmpty())
                    elixir.ingredients.forEach { ingredient ->
                        ElixirDetailListItem(ingredient.name)
                    }
                else
                    NoDataField()
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh,
                        RoundedCornerShape(15f)
                    ).padding(8.dp)
            ) {
                Spacer(Modifier.height(8.dp))
                // Inventors list
                SectionLabel("Inventors")
                Spacer(Modifier.height(8.dp))
                if (elixir.inventors.isNotEmpty())
                    elixir.inventors.forEach { inventor ->
                        ElixirDetailListItem(inventor.getName())
                    }
                else
                    NoDataField()
            }
        }
    }
}

@Composable
fun ElixirDetailListItem(value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = "- $value",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun SectionLabel(
    label: String
) {
    Text(
        text = "$label:",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Normal
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Characteristics(label: String, value: String) {
    val items = remember(value) { value.split(";") }
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        SectionLabel("Characteristics")
        if (value.isNotBlank())
            FlowRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh,
                        RoundedCornerShape(15f)
                    )
                    .padding(8.dp),
                content = {
                    items.forEach { characteristic ->
                        AssistChip(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            onClick = {},
                            label = {
                                Text(
                                    text = characteristic,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            elevation = AssistChipDefaults.elevatedAssistChipElevation(2.dp)
                        )
                    }
                }
            )
        else
            NoDataField()
    }
}

@Composable
fun ElixirDetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        SectionLabel(label)
        if (value.isNotBlank())
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHigh,
                        RoundedCornerShape(15f)
                    )
                    .padding(8.dp)
                    .padding(bottom = 12.dp)
            )
        else
            NoDataField()

    }
}

@Composable
fun NoDataField() {
    Text(
        text = "not available",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceContainerLowest,
                RoundedCornerShape(15f)
            ).padding(8.dp),
        fontStyle = FontStyle.Italic
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewElixirScreen() {
    ElixirSheet(
        elixir =
        Elixir(
            characteristics = "Translucent; shiny liquid;Translucent; shiny liquid",
            difficulty = "Hard",
            effect = "Grants temporary invisibility",
            ingredients = listOf(Ingredient(name = "Bat wings"), Ingredient(name = "Fairy dust")),
            inventors = listOf(
                Inventor("Severus Snape"),
                Inventor("Severus Snape"),
                Inventor("Severus Snape")
            ),
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
