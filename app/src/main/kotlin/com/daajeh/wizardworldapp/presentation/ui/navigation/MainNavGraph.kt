package com.daajeh.wizardworldapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daajeh.wizardworldapp.presentation.ui.details.ElixirDetailsScreen
import com.daajeh.wizardworldapp.presentation.ui.home.ElixirsScreen
import com.daajeh.wizardworldapp.presentation.ui.home.ElixirsViewModel

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    homeViewModel: ElixirsViewModel,
    startDestination: Screen = Screen.ElixirList
) {
    val navController =
        rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Screen.ElixirList.route) {
            val elixirs by homeViewModel.elixirs.collectAsStateWithLifecycle()

            ElixirsScreen(
                elixirs = elixirs,  // Use actual data from ViewModel
                details = { elixirId ->
                    homeViewModel.load(elixirId)
                    navController.navigate(Screen.ElixirDetails.route)
                }
            )
        }

        composable(Screen.ElixirDetails.route) {
            val elixir by homeViewModel.elixir.collectAsStateWithLifecycle()

            ElixirDetailsScreen(
                elixir = elixir,
                onToggleFavourite = homeViewModel::toggleElixirFavouriteState
            )
        }
    }
}



