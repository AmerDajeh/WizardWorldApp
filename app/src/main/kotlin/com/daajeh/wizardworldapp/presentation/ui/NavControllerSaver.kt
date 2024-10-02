package com.daajeh.wizardworldapp.presentation.ui

import android.content.Context
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import kotlinx.coroutines.flow.map
//
//// Create a custom NavControllerSaver
//fun NavControllerSaver(): Saver<NavController, Any> = mapSaver(
//    save = { mapOf("currentBackStack" to it.currentBackStackEntryFlow.map(NavBackStackEntry::destination)) },
//    restore = {
//        // Restore the navigation state here (custom logic might be needed)
//        val restoredNavController = NavController(it["context"] as Context)
//        val restoredBackStack = it["currentBackStack"] as List<NavGraph>
//
//        // Repopulate backstack logic (could require custom handling)
//        restoredBackStack.forEach { graph ->
//            restoredNavController.graph = graph
//        }
//
//        restoredNavController
//    }
//)
