package com.daajeh.wizardworldapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daajeh.wizardworldapp.presentation.ui.details.WizardDetailsScreen
import com.daajeh.wizardworldapp.presentation.ui.home.WizardsScreen
import com.daajeh.wizardworldapp.presentation.ui.home.WizardsViewModel
import com.daajeh.wizardworldapp.work.FetchElixirDataWorker
import com.daajeh.wizardworldapp.work.FetchWizardDataWorker

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    viewModel: WizardsViewModel,
    startDestination: Screen = Screen.ElixirList
) {
    val context = LocalContext.current

    val navController =
        rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Screen.ElixirList.route) {
            val wizards by viewModel.wizards.collectAsStateWithLifecycle()
            val error by viewModel.error.collectAsStateWithLifecycle()

            WizardsScreen(
                wizards = wizards,  // Use actual data from ViewModel
                error = error,
                details = { wizardId ->
                    viewModel.load(wizardId)
                    FetchWizardDataWorker.enqueue(context, wizardId)
                    navController.navigate(Screen.WizardDetails.route)
                }
            )
        }

        composable(Screen.WizardDetails.route) {
            val wizard by viewModel.wizard.collectAsStateWithLifecycle()
            val elixir by viewModel.elixir.collectAsStateWithLifecycle()

            WizardDetailsScreen(
                wizard = wizard,
                elixir = elixir,
                onLoadElixir = { elixirId ->
                    FetchElixirDataWorker.enqueue(context, elixirId)
                    viewModel.loadElixir(elixirId)
                },
                onBottomSheetClosed = { viewModel.loadElixir("") },
                onToggleFavouriteWizard = viewModel::toggleWizardFavouriteState,
                onToggleFavouriteElixir = viewModel::toggleElixirFavouriteState,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}



