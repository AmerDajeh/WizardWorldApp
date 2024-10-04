package com.daajeh.wizardworldapp.domain.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@Stable
data class Ingredient(
    val id: String = "",
    val name: String = ""
)