package com.kevin.timenote.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.kevin.timenote.R
import com.kevin.timenote.base.LocalNavController
import com.kevin.timenote.ui.navigation.TimeRoute
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen() {
    val navController = LocalNavController.current
    LaunchedEffect("") {
        delay(1000)
        navController.navigate(TimeRoute.Home) { popUpTo<TimeRoute.Welcome> { inclusive = true } }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.ic_welcome),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

}