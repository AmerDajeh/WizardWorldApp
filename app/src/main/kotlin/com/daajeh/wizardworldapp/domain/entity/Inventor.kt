package com.daajeh.wizardworldapp.domain.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@Stable
data class Inventor(
    val firstName: String = "",
    val id: String = "",
    val lastName: String = ""
) {
    fun getName(): String {
        val first = if (firstName.isNotBlank()) "$firstName " else ""
        return "$first$lastName"
    }
}