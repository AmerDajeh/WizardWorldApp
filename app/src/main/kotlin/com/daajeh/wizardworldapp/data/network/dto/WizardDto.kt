package com.daajeh.wizardworldapp.data.network.dto

import com.daajeh.wizardworldapp.data.local.dto.wizard.WizardEntity
import kotlinx.serialization.Serializable

@Serializable
data class WizardDto(
    val elixirs: List<LightElixirDto>,
    val firstName: String? = "",
    val id: String,
    val lastName: String? = ""
) {
    fun toEntity() =
        WizardEntity(
            id = id,
            firstName = firstName ?: "",
            lastName = lastName ?: ""
        )
}