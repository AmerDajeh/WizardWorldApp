package com.daajeh.wizardworldapp.data.network.dto

import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.LightElixirEntity
import kotlinx.serialization.Serializable

@Serializable
data class LightElixirDto(
    val id: String = "",
    val name: String = "",
) {

    fun toEntity(wizardId: String) =
        LightElixirEntity(
            id = id,
            wizardId = wizardId,
            name=name
        )
}