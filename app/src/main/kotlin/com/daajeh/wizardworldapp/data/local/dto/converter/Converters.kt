package com.daajeh.wizardworldapp.data.local.dto.converter

import androidx.room.TypeConverter
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ingredient.IngredientEntity
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.inventor.InventorEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromElixirsList(ingredients: List<ElixirEntity>): String {
        return json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toElixirsList(data: String): List<ElixirEntity> {
        return json.decodeFromString(data)
    }

    @TypeConverter
    fun fromIngredientList(ingredients: List<IngredientEntity>): String {
        return json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredientList(data: String): List<IngredientEntity> {
        return json.decodeFromString(data)
    }

    @TypeConverter
    fun fromInventorList(inventors: List<InventorEntity>): String {
        return json.encodeToString(inventors)
    }

    @TypeConverter
    fun toInventorList(data: String): List<InventorEntity> {
        return json.decodeFromString(data)
    }
}
