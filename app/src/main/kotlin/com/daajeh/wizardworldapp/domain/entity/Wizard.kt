package com.daajeh.wizardworldapp.domain.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@Stable
data class Wizard(
    val elixirs: List<LightElixir> = listOf(),
    val traits: List<String> = listOf(),
    val firstName: String = "",
    val id: String = "",
    val lastName: String = "",
    val isFavorite: Boolean = false
) {

    fun getName() =
        "$firstName $lastName"

}