package com.kevin.timenote.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kevin.timenote.ui.home.HomeScreen
import com.kevin.timenote.ui.mine.MineScreen

@Composable
fun MainScreen(navController: NavController, modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }

    Column {
        Row {
            TabButton("Home", selectedTab == 0) { selectedTab = 0 }
            TabButton("Mine", selectedTab == 1) { selectedTab = 1 }
        }

        when (selectedTab) {
            0 -> HomeScreen(navController)
            1 -> MineScreen(navController)
        }
    }
}

@Composable
fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .padding(16.dp)
            .clickable(onClick = onClick),
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
    )
}
