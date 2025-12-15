package com.kevin.timenote.ui.navigation

sealed class Route(val route: String) {
    object Main : Route("main")
    object Home : Route("home")
    object Mine : Route("mine")
    object Countdown : Route("countdown")
}
