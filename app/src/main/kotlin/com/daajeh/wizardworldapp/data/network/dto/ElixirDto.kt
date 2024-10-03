package com.daajeh.wizardworldapp.data.network.dto

import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ElixirEntity
import kotlinx.serialization.Serializable

@Serializable
data class ElixirDto(
    val characteristics: String? = "",
    val difficulty: String? = "",
    val effect: String? = "",
    val id: String = "",
    val ingredients: List<IngredientDto> = listOf(),
    val inventors: List<InventorDto> = listOf(),
    val manufacturer: String?? = "",
    val name: String?? = "",
    val sideEffects: String? = "",
    val time: String? = ""
) {
    fun toEntity(wizardId: String) =
        ElixirEntity(
            id = id,
            wizardId = wizardId,
            characteristics = characteristics ?: "",
            difficulty = difficulty ?: "",
            effect = effect ?: "",
            manufacturer = manufacturer ?: "",
            name = name ?: "",
            sideEffects = sideEffects  ?: "",
            time = time ?: ""
        )

    fun getIngredientEntities() =
        ingredients
            .map { it.toEntity(elixirId = this.id) }

    fun getInventorEntities() =
        inventors
            .map { it.toEntity(elixirId = this.id) }
}