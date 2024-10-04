package com.daajeh.wizardworldapp.domain.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@Stable
data class Elixir(
    val characteristics: String = "",
    val difficulty: String = "",
    val effect: String = "",
    val id: String = "",
    val ingredients: List<Ingredient> = listOf(),
    val inventors: List<Inventor> = listOf(),
    val manufacturer: String = "",
    val name: String = "",
    val sideEffects: String = "",
    val time: String = "",
    val isFavorite: Boolean = false
)