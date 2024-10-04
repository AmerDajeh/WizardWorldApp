package com.daajeh.wizardworldapp.presentation.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.daajeh.wizardworldapp.presentation.ui.details.WizardDetailsScreen
import com.daajeh.wizardworldapp.presentation.ui.details.WizardDetailsViewModel
import com.daajeh.wizardworldapp.presentation.ui.home.WizardsScreen
import com.daajeh.wizardworldapp.presentation.ui.home.WizardsViewModel
import com.daajeh.wizardworldapp.presentation.ui.sheet.ElixirBottomSheet
import com.daajeh.wizardworldapp.presentation.ui.sheet.ElixirBottomSheetViewModel
import com.daajeh.wizardworldapp.work.FetchElixirDataWorker
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    startDestination: Screen = Screen.ElixirList
) {
    val context = LocalContext.current
    val navController =
        rememberNavController()
    val wizardsViewModel: WizardsViewModel = koinViewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Screen.ElixirList.route) {
            val wizards by wizardsViewModel.wizards.collectAsStateWithLifecycle()
            val error by wizardsViewModel.error.collectAsStateWithLifecycle()

            WizardsScreen(
                wizards = wizards,
                error = error,
                details = { wizardId ->
                    navController.navigate(Screen.WizardDetails.route.plus("/$wizardId"))
                }
            )
        }

        composable(Screen.WizardDetails.route.plus("/{wizardId}")) {
            val viewModel: WizardDetailsViewModel = koinViewModel<WizardDetailsViewModel>(scope = wizardsViewModel.scope)

            val wizard by viewModel.wizard.collectAsStateWithLifecycle()

            WizardDetailsScreen(
                wizard = wizard,
                onToggleFavouriteWizard = viewModel::toggleWizardFavouriteState,
                onBackClick = {
                    navController.popBackStack()
                },
                navigateToBottomSheet = { elixirId ->
                    FetchElixirDataWorker.enqueue(context, elixirId)
                    navController.navigate(Screen.ElixirSheet.route.plus("/$elixirId"))
                }
            )
        }

        dialog(Screen.ElixirSheet.route.plus("/{elixirId}")) {
            val bottomSheetState = rememberModalBottomSheetState()
            val viewModel: ElixirBottomSheetViewModel = koinViewModel(scope = wizardsViewModel.scope)

            val elixir by viewModel.elixir.collectAsStateWithLifecycle()
            val error by viewModel.error.collectAsStateWithLifecycle()

            ModalBottomSheet(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth(),
                onDismissRequest = {
                    navController.popBackStack()
                },
                sheetState = bottomSheetState,
                properties = ModalBottomSheetProperties(shouldDismissOnBackPress = true)

            ) {
                ElixirBottomSheet(
                    elixir = elixir,
                    error = error,
                    onToggleFavorite = viewModel::toggleElixirFavouriteState
                )
            }
        }
    }
}



