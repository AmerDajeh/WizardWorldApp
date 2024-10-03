package com.daajeh.wizardworldapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daajeh.wizardworldapp.presentation.ui.details.WizardDetailsScreen
import com.daajeh.wizardworldapp.presentation.ui.home.WizardsScreen
import com.daajeh.wizardworldapp.presentation.ui.home.WizardsViewModel

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    homeViewModel: WizardsViewModel,
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
            val wizards by homeViewModel.wizards.collectAsStateWithLifecycle()

            WizardsScreen(
                wizards = wizards,  // Use actual data from ViewModel
                details = { elixirId ->
                    homeViewModel.load(elixirId)
                    navController.navigate(Screen.WizardDetails.route)
                }
            )
        }

        composable(Screen.WizardDetails.route) {
            val wizard by homeViewModel.wizard.collectAsStateWithLifecycle()

            WizardDetailsScreen(
                wizard = wizard,
                onToggleFavourite = homeViewModel::toggleWizardFavouriteState,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}



