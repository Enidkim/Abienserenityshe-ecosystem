package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.EcosystemViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup edge-to-edge content drawing under physical status/nav bars
        enableEdgeToEdge()
        
        setContent {
            MyApplicationTheme {
                val ecosystemViewModel: EcosystemViewModel = viewModel()
                val currentScreen by ecosystemViewModel.currentScreen.collectAsState()

                // Capture system Android back clicks for sub-screen back exits
                BackHandler(enabled = currentScreen != Screen.HOME) {
                    ecosystemViewModel.goBack()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    // Full bleeding fluid screen fade transitions
                    Crossfade(
                        targetState = currentScreen,
                        animationSpec = tween(350),
                        label = "screenTransition"
                    ) { target ->
                        when (target) {
                            Screen.HOME -> HomeScreen(viewModel = ecosystemViewModel)
                            Screen.CREATIVE_LAB -> CreativeLabScreen(viewModel = ecosystemViewModel)
                            Screen.BOUTIQUE -> BoutiqueStoreScreen(viewModel = ecosystemViewModel)
                            Screen.WELLNESS -> WellnessScreen(viewModel = ecosystemViewModel)
                            Screen.LIFESTYLE -> LifestyleMediaScreen(viewModel = ecosystemViewModel)
                        }
                    }
                }
            }
        }
    }
}
