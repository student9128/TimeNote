package com.kevin.timenote.ui.navigation

sealed class Route(val route: String) {
    object Main : Route("main")
    object Countdown : Route("countdown")
}
