package com.daajeh.wizardworldapp.presentation.ui.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.Ingredient
import com.daajeh.wizardworldapp.domain.entity.Inventor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElixirDetailsScreen(
    elixir: Elixir?,
    onToggleFavourite: (String) -> Unit
) {
    AnimatedContent(elixir != null){
        when {
            it -> elixir?.let{
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = elixir.name) },
                            actions = {
                                AnimatedHeartButton(
                                    modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                                    isFavourite = elixir.isFavourite,
                                    onClick = { onToggleFavourite(elixir.id) }
                                )
                            }
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues) // Only padding from Scaffold
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Manufacturer: ${elixir.manufacturer}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        InfoText(label = "Difficulty", value = elixir.difficulty)
                        InfoText(label = "Effect", value = elixir.effect)
                        InfoText(label = "Time", value = elixir.time)

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionTitle(title = "Characteristics")
                        Text(
                            text = elixir.characteristics,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionTitle(title = "Ingredients")
                        LazyColumn {
                            items(elixir.ingredients) { ingredient ->
                                Text(
                                    text = "• ${ingredient.name}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionTitle(title = "Inventors")
                        LazyColumn {
                            items(elixir.inventors) { inventor ->
                                Text(
                                    text = "${inventor.firstName} ${inventor.lastName}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionTitle(title = "Side Effects")
                        Text(text = elixir.sideEffects, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            else -> ElixirNotFound(modifier = Modifier)
        }
    }

}

@Composable
fun InfoText(label: String, value: String) {
    Column {
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp)) // Optional: add space between fields
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun AnimatedHeartButton(
    modifier: Modifier = Modifier,
    isFavourite: Boolean,
    onClick: () -> Unit
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isFavourite) Color.Red else Color.Gray,
        animationSpec = tween(durationMillis = 300) // Customize duration as needed
    )

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onClick()
                    }
                )
            }
    ) {
        val path = Path().apply {
            moveTo(size.width* 0.5f, size.height * 0.4f) // Bottom tip of heart
            cubicTo(
                size.width * 0.3f, size.height * 0.2f, // Left curve
                0f, size.height * 0.4f, // Left bottom point
                size.width* 0.5f, size.height * 0.8f // Bottom point of heart
            )
            cubicTo(
                size.width, size.height * 0.4f, // Right bottom point
                size.width * 0.7f, size.height * 0.2f, // Right curve
                size.width* 0.5f, size.height *0.4f // Bottom tip of heart
            )
            close()
        }

        drawPath(
            path = path,
            color = animatedColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ElixirDetailsPreview() {
    var sampleElixir by remember {
        mutableStateOf(
            Elixir(
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
                time = "5 minutes",
                isFavourite = false
            )
        )
    }

    ElixirDetailsScreen(
        elixir = sampleElixir,
        onToggleFavourite = {
            sampleElixir = sampleElixir.copy(isFavourite = !sampleElixir.isFavourite)
        }
    )
}


@Composable
private fun ElixirNotFound(
    modifier: Modifier = Modifier
) {
    // todo
}