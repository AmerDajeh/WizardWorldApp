package com.daajeh.wizardworldapp.data.local.dto

import androidx.room.Embedded
import androidx.room.Relation

data class ElixirWithIngredients(
    @Embedded val elixir: ElixirEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "elixirId"
    )
    val ingredients: List<IngredientEntity>
)
