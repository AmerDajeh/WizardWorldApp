package com.daajeh.wizardworldapp.data.network.dto

import com.daajeh.wizardworldapp.data.local.dto.InventorEntity
import kotlinx.serialization.Serializable

@Serializable
data class InventorDto(
    val firstName: String = "",
    val id: String = "",
    val lastName: String = ""
) {
    fun toEntity(elixirId: String) =
        InventorEntity(
            firstName = firstName,
            id = id,
            lastName = lastName,
            elixirId = elixirId
        )
}