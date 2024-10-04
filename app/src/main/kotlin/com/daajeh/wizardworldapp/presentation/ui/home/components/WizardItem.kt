package com.daajeh.wizardworldapp.presentation.ui.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daajeh.wizardworldapp.R
import com.daajeh.wizardworldapp.domain.entity.Wizard


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.WizardListItem(
    animatedVisibilityScope: AnimatedVisibilityScope,
    wizard: Wizard,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .sharedElement(
                state = rememberSharedContentState("wizard-${wizard.id}"),
                animatedVisibilityScope = animatedVisibilityScope
            )
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(wizard.id) },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = wizard.getName(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Elixirs Count: ${wizard.elixirs.size}",
                    style = MaterialTheme.typography.labelMedium
                )
            }

            WizardIcon(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState("image-${wizard.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WizardListItemPreview() {
    val sampleWizard = Wizard(
        id = "1",
//        name = "Healing Elixir",
        firstName = "Medium",
        lastName = "Salem",
        elixirs = listOf()
    )

//    WizardListItem(wizard = sampleWizard, onClick = {})
}

@Composable
fun WizardIcon(
    modifier: Modifier = Modifier
){
    val color = remember { getRandomBrightModernColor() }
    Icon(
        painter = painterResource(R.drawable.ic_wizard),
        contentDescription = stringResource(R.string.wizard),
        tint = color,
        modifier = modifier
    )
}

fun getRandomBrightModernColor(): Color {
    val brightModernColors = listOf(
        Color(0xFFFFC857),  // Bright Soft Yellow
        Color(0xFFFF6B6B),  // Bright Coral
        Color(0xFFFF9F1C),  // Vivid Orange
        Color(0xFFBCE784),  // Bright Green
        Color(0xFF2EC4B6),  // Bright Teal
        Color(0xFFFDFFB6),  // Soft Pastel Yellow
        Color(0xFF9BF6FF),  // Soft Cyan
        Color(0xFFFFD6E0),  // Soft Pink
        Color(0xFFA0E426),  // Neon Green
        Color(0xFFFFCA3A),  // Bright Gold
        Color(0xFFFFE066),  // Soft Lemon Yellow
        Color(0xFFFAA307),  // Rich Orange-Yellow
        Color(0xFFFFD670),  // Soft Peach
        Color(0xFFFF8C42),  // Vivid Tangerine
        Color(0xFFEA698B),  // Bright Magenta
        Color(0xFFB4E197),  // Pastel Green
        Color(0xFFDAF7A6),  // Fresh Lime
        Color(0xFFFFADAD)   // Soft Coral Pink
    )

    return brightModernColors.random()
}

