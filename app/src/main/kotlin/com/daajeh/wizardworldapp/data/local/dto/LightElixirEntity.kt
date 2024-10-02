package com.daajeh.wizardworldapp.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daajeh.wizardworldapp.domain.entity.LightElixir
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "light_elixirs")
data class LightElixirEntity(
    @PrimaryKey val id: String,
    val name: String = "",
    val difficulty: String = "",
) {

    fun toDomain(
        isFavourite: Boolean = false
    ) =
        LightElixir(
            name = name,
            difficulty = difficulty,
            id = id,
            isFavourite = isFavourite
        )
}
