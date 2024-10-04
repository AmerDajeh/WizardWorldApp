package com.daajeh.wizardworldapp.domain.entity

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