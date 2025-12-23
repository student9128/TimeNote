package com.kevin.timenote.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable


@Serializable
sealed class TimeRoute {
    @Serializable
    object Home

    @Serializable
    object Mine

    @Serializable
    object Countdown

    @Serializable
    data class CountdownDetail(val id: Long)
}


enum class Destination(
    val route: Any,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    Home(TimeRoute.Home, "Home", Icons.Default.Home, "Home"),
    Mine(TimeRoute.Mine, "Mine", Icons.Default.Person, "Mine"),
}