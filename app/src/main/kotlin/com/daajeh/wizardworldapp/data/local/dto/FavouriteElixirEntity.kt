package com.daajeh.wizardworldapp.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "favourite_elixirs")
data class FavouriteElixirEntity(
    @PrimaryKey val id: String
)