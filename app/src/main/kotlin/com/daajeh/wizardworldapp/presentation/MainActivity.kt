package com.daajeh.wizardworldapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.daajeh.wizardworldapp.presentation.ui.navigation.MainNavGraph
import com.daajeh.wizardworldapp.presentation.ui.home.WizardsViewModel
import com.daajeh.wizardworldapp.presentation.ui.theme.WizardWorldAppTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val homeViewModel by viewModel<WizardsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinAndroidContext {
                WizardWorldAppTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MainNavGraph(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = homeViewModel
                        )
                    }
                }
            }
        }
    }
}