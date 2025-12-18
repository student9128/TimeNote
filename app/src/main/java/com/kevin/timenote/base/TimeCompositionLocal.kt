package com.kevin.timenote.base

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

val LocalNavController = staticCompositionLocalOf<NavController> { error("NavController not provided") }
val LocalToast = staticCompositionLocalOf<UIActions> { error("NavController not provided") }