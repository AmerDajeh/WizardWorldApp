package com.daajeh.wizardworldapp.presentation.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object ElixirList : Screen("elixirList")

    @Serializable
    data object ElixirDetails : Screen("elixirDetails")
}