package com.daajeh.wizardworldapp.data.local.dto

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    private val json = Json { ignoreUnknownKeys = true }

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
