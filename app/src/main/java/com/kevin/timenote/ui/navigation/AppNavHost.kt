package com.kevin.timenote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.kevin.timenote.ui.countdown.CountdownDetailScreen
import com.kevin.timenote.ui.countdown.CountdownScreen
import com.kevin.timenote.ui.home.HomeScreen
import com.kevin.timenote.ui.mine.MineScreen

//@Composable
//fun AppNavGraph(navController: NavHostController,modifier: Modifier = Modifier) {
////    val navController = rememberNavController()
//    NavHost(navController, Route.Home) {
////        composable(Route.Main.route) {
////            MainScreen(navController)
////        }
//        composable<Route.Home> {HomeScreen()  }
//        composable(route = "Home") {
//            HomeScreen(navController)
//        }
//        composable(route = "Mine") {
//            MineScreen(navController)
//        }
//        composable("Countdown?model={model}", arguments = listOf(navArgument("model"){type= NavType.StringType})) {
//            CountdownScreen(navController)
//        }
//    }
//}
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Any,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination
    ) {
        composable<TimeRoute.Home> { HomeScreen() }
        composable<TimeRoute.Mine> { MineScreen() }
//        Destination.entries.forEach { destination ->
//            composable(destination.route) {
//                when (destination) {
//                    Destination.Home -> HomeScreen()
//                    Destination.Mine -> MineScreen()
//                }
//            }
//        }
        composable<TimeRoute.Countdown>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "timenote://add_event"
                }
            )
        ) { CountdownScreen() }
//        composable<TimeRoute.CountdownEdit> { CountdownScreen() }
        composable<TimeRoute.CountdownDetail>(
            deepLinks = listOf(
                navDeepLink<TimeRoute.CountdownDetail>(basePath = "timenote://detail")
            )
        ) { CountdownDetailScreen() }
    }
}
