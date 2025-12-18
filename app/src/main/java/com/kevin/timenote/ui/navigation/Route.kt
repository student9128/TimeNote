package com.kevin.timenote.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed class Route(val route: String) {
    object Main : Route("main")
    object Home : Route("home")
    object Mine : Route("mine")
    object Countdown : Route("countdown")
}

@Serializable
object Home

@Serializable
object Mine

@Serializable
object Countdown

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
){
    Home("Home", "Home", Icons.Default.Home, "Home"),
    Mine("Mine", "Mine", Icons.Default.Person, "Mine"),
}