package com.daajeh.wizardworldapp.data.local.dto.wizard

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daajeh.wizardworldapp.domain.entity.LightElixir
import com.daajeh.wizardworldapp.domain.entity.Wizard
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "wizards")
data class WizardEntity(
    val firstName: String,
    @PrimaryKey
    val id: String,
    val lastName: String
) {
    fun toDomain(
        elixirs: List<LightElixir>,
        isFavourite: Boolean
    ) = Wizard(
        id = id,
        firstName = firstName,
        lastName = lastName,
        elixirs = elixirs,
        isFavorite = isFavourite
    )
}