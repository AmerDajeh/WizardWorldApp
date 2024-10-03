package com.daajeh.wizardworldapp.data.local.dto.favourite

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "favourite_wizards")
data class FavouriteWizardEntity(
    @PrimaryKey val id: String
)