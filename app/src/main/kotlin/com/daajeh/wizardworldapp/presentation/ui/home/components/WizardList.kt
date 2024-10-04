package com.daajeh.wizardworldapp.presentation.ui.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daajeh.wizardworldapp.domain.entity.Wizard

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.WizardList(
    animatedVisibilityScope: AnimatedVisibilityScope,
    wizards: List<Wizard>,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 8.dp)
            .background(
                MaterialTheme.colorScheme.surfaceContainerLowest,
                MaterialTheme.shapes.extraSmall
            )
    ) {
        if (wizards.isNotEmpty())
            item {

                Column(
                    Modifier.fillMaxWidth().background(
                        MaterialTheme.colorScheme.surfaceContainerHighest,
                        MaterialTheme.shapes.small
                    ).padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "Wizards",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "fetched ${wizards.size} wizards",
                        style = MaterialTheme.typography.labelSmall,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        item { Spacer(Modifier.height(24.dp)) }
        items(wizards) { wizard ->
            WizardListItem(
                animatedVisibilityScope = animatedVisibilityScope,
                wizard = wizard,
                onClick = onItemClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WizardListPreview() {
    val sampleWizards = listOf(
        Wizard(id = "1", firstName = "Healing Elixir", lastName = "", elixirs = listOf()),
        Wizard(id = "2", firstName = "Power Elixir", lastName = "", elixirs = listOf())
    )

//    WizardList(wizards = sampleWizards, onItemClick = {})
}
