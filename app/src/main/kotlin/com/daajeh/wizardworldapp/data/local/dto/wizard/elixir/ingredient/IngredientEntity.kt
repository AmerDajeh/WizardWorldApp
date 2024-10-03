package com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ingredient

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daajeh.wizardworldapp.domain.entity.Ingredient
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey val id: String,
    val name: String = "",
    val elixirId: String
){

    fun toDomain() =
        Ingredient(
            id = this.id,
            name = this.name
        )
}
