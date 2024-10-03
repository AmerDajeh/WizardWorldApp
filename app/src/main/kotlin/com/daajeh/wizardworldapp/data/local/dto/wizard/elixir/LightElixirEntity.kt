package com.daajeh.wizardworldapp.data.local.dto.wizard.elixir

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daajeh.wizardworldapp.domain.entity.LightElixir
import kotlinx.serialization.Serializable

@Serializable
@Entity(primaryKeys = ["id", "wizardId"], tableName = "light_elixirs")
data class LightElixirEntity(
    val id: String,
    val wizardId: String,
    val name: String
) {

    fun toDomain() =
        LightElixir(
            id = id,
            wizardId = wizardId,
            name = name
        )
}
