package com.daajeh.wizardworldapp.data.local.dto.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ingredient.IngredientEntity

data class ElixirWithIngredients(
    @Embedded val elixir: ElixirEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "elixirId"
    )
    val ingredients: List<IngredientEntity>
)
