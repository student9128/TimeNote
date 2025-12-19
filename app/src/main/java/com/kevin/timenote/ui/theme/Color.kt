package com.kevin.timenote.ui.theme

import androidx.compose.animation.core.copy
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/* =========================================================
 * Light Theme Tokens
 * ========================================================= */

// Primary
val PrimaryLight = Color(0xFF7C63D5)
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFEDE0FF)
val OnPrimaryContainerLight = Color(0xFF2A0051)
val InversePrimaryLight = Color(0xFFD3B9FF)

// Secondary
val SecondaryLight = Color(0xFF625B71)
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFE9DDFF)
val OnSecondaryContainerLight = Color(0xFF1F0C3D)

// Tertiary
val TertiaryLight = Color(0xFF7B5260)
val OnTertiaryLight = Color(0xFFFFFFFF)
val TertiaryContainerLight = Color(0xFFFFD9E1)
val OnTertiaryContainerLight = Color(0xFF330013)

// Background / Surface
val BackgroundLight = Color(0xFFF7F5FA)
val OnBackgroundLight = Color(0xFF1C1B1E)

val SurfaceLight = Color(0xFFF7F5FA)
val OnSurfaceLight = Color(0xFF1C1B1E)

val SurfaceVariantLight = Color(0xFFE7E0EB)
val OnSurfaceVariantLight = Color(0xFF49454F)

val SurfaceTintLight = PrimaryLight
val InverseSurfaceLight = Color(0xFF323033)
val InverseOnSurfaceLight = Color(0xFFF5EFF4)

// Error
val ErrorLight = Color(0xFFBA1A1A)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFFDAD6)
val OnErrorContainerLight = Color(0xFF410002)


// Outline/Scrim
val OutlineLight = Color(0xFF79747E)
val OutlineVariantLight = Color(0xFFD0C6DB)
val ScrimLight = Color(0xFF000000)

// Surface elevation (M3)
val SurfaceBrightLight = Color(0xFFF9F7FC)
val SurfaceDimLight = Color(0xFFECE8F2)
val SurfaceContainerLowestLight = Color(0xFFF8F6FB)
val SurfaceContainerLowLight = Color(0xFFF7F4FA)
val SurfaceContainerLight = Color(0xFFF5F3F8)
val SurfaceContainerHighLight = Color(0xFFF3F1F6)
val SurfaceContainerHighestLight = Color(0xFFF1EFF4)


/* =========================================================
 * Dark Theme Tokens
 * ========================================================= */

// Primary
val PrimaryDark = Color(0xFFD0BCFF)
val OnPrimaryDark = Color(0xFF381E72)
val PrimaryContainerDark = Color(0xFF4F378B)
val OnPrimaryContainerDark = Color(0xFFEADDFF)
val InversePrimaryDark = Color(0xFF6750A4)

// Secondary
val SecondaryDark = Color(0xFFCCC2DC)
val OnSecondaryDark = Color(0xFF332D41)
val SecondaryContainerDark = Color(0xFF4A4458)
val OnSecondaryContainerDark = Color(0xFFE8DEF8)

// Tertiary
val TertiaryDark = Color(0xFFEFB8C8)
val OnTertiaryDark = Color(0xFF492532)
val TertiaryContainerDark = Color(0xFF633B48)
val OnTertiaryContainerDark = Color(0xFFFFD8E4)

// Background / Surface
val BackgroundDark = Color(0xFF1C1B1F)
val OnBackgroundDark = Color(0xFFE6E1E5)

val SurfaceDark = Color(0xFF1C1B1F)
val OnSurfaceDark = Color(0xFFE6E1E5)

val SurfaceVariantDark = Color(0xFF49454F)
val OnSurfaceVariantDark = Color(0xFFCAC4D0)

val InverseSurfaceDark = Color(0xFFE6E1E5)
val InverseOnSurfaceDark = Color(0xFF313033)

// Error
val ErrorDark = Color(0xFFF2B8B5)
val OnErrorDark = Color(0xFF601410)
val ErrorContainerDark = Color(0xFF8C1D18)
val OnErrorContainerDark = Color(0xFFF9DEDC)

// Outline
val OutlineDark = Color(0xFF938F99)
val OutlineVariantDark = Color(0xFF49454F)

// Scrim
val ScrimDark = Color(0xFF000000)

// Surface elevation (M3)
val SurfaceDimDark = Color(0xFF141218)
val SurfaceBrightDark = Color(0xFF3B383E)

val SurfaceContainerLowestDark = Color(0xFF0F0D13)
val SurfaceContainerLowDark = Color(0xFF1D1B20)
val SurfaceContainerDark = Color(0xFF211F26)
val SurfaceContainerHighDark = Color(0xFF2B2930)
val SurfaceContainerHighestDark = Color(0xFF36343B)
