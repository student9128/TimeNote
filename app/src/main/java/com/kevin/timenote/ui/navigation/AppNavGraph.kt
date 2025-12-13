package com.kevin.timenote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController, Route.Main.route) {
        composable(Route.Main.route) {
            MainScreen(navController)
        }
        composable(Route.Countdown.route) {
            CountdownScreen(navController)
        }
    }
}
