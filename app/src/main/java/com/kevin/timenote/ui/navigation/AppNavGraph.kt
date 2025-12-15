package com.kevin.timenote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kevin.timenote.ui.countdown.CountdownScreen
import com.kevin.timenote.ui.home.HomeScreen
import com.kevin.timenote.ui.main.MainScreen
import com.kevin.timenote.ui.mine.MineScreen

@Composable
fun AppNavGraph(navController: NavHostController,modifier: Modifier = Modifier) {
//    val navController = rememberNavController()
    NavHost(navController, "Home") {
//        composable(Route.Main.route) {
//            MainScreen(navController)
//        }
        composable(route = "Home") {
            HomeScreen(navController)
        }
        composable(route = "Mine") {
            MineScreen(navController)
        }
        composable("Countdown?model={model}", arguments = listOf(navArgument("model"){type= NavType.StringType})) {
            CountdownScreen(navController)
        }
    }
}
