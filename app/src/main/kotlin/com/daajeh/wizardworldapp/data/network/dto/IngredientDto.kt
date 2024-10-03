package com.daajeh.wizardworldapp.data.network.dto

import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ingredient.IngredientEntity
import kotlinx.serialization.Serializable

@Serializable
data class IngredientDto(
    val id: String = "",
    val name: String? = ""
) {
    fun toEntity(elixirId: String) =
        IngredientEntity(
            id = id,
            name= name ?: "",
            elixirId = elixirId
        )
}