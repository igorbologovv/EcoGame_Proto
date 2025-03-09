package com.example.econbeacon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.econbeacon.ui.screens.MainScreen
import com.example.econbeacon.ui.screens.MarketScreen
import com.example.econbeacon.ui.theme.EconBeaconTheme
import com.example.econbeacon.viewmodel.GameStateViewModel
import com.example.econbeacon.viewmodel.MarketViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EconBeaconTheme {
                MyApp()
            }
        }
    }
}
@Composable
fun MyApp() {

    val navController = rememberNavController()
    val gameStateViewModel: GameStateViewModel = viewModel()

    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        gameStateViewModel.initGameState(context)
    }

    NavHost(
        navController = navController,
        startDestination = "main_screen"
    ) {
        composable("main_screen") {
            MainScreen(
                onMarketClick = {
                    navController.navigate("market_screen")
                },
                gameStateViewModel = gameStateViewModel
            )
        }

        composable("market_screen") {
            val marketVM: MarketViewModel = viewModel()
            MarketScreen(
               //
                marketViewModel = marketVM,
                gameStateViewModel = gameStateViewModel,
                onBack = { navController.navigateUp() }
            )
        }
    }
}

