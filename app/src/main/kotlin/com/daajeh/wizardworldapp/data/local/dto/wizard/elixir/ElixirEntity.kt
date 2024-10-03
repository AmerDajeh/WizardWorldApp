package com.daajeh.wizardworldapp.data.local.dto.wizard.elixir

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.Ingredient
import com.daajeh.wizardworldapp.domain.entity.Inventor
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "elixirs")
data class ElixirEntity(
    @PrimaryKey val id: String,
    val wizardId: String,
    val characteristics: String = "",
    val difficulty: String = "",
    val effect: String = "",
    val manufacturer: String = "",
    val name: String = "",
    val sideEffects: String = "",
    val time: String = ""
) {

    fun toDomain(
        ingredients: List<Ingredient>,
        inventors: List<Inventor>,
        isFavourite: Boolean = false
    ) =
        Elixir(
            characteristics = this.characteristics,
            difficulty = this.difficulty,
            effect = this.effect,
            id = this.id,
            ingredients = ingredients,
            inventors = inventors,
            manufacturer = this.manufacturer,
            name = this.name,
            sideEffects = this.sideEffects,
            time = this.time,
            isFavorite = isFavourite
        )
}
