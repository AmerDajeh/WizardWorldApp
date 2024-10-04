package com.daajeh.wizardworldapp.domain.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@Stable
data class LightElixir(
    val id: String = "",
    val wizardId: String = "",
    val name: String = "",
)