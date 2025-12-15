package com.kevin.timenote.ui.mine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kevin.timenote.ui.widget.AppBar

@Composable
fun MineScreen(navController: NavController) {
    Scaffold(topBar = { AppBar(title = "Home", showBackIcon = false) }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Mine")
        }
    }

}