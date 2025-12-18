package com.kevin.timenote.ui.home

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.kevin.timenote.ui.countdown.CountdownItem
import com.kevin.timenote.ui.navigation.LocalNavController
import com.kevin.timenote.ui.navigation.Route
import com.kevin.timenote.ui.widget.AppBar
import com.nlf.calendar.Solar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val list by viewModel.countdowns.collectAsState()
    val navController = LocalNavController.current
//
    Scaffold(   topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text("Small Top App Bar")
            }
        )
    },) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {
            Text("${Solar.fromYmd(2025,12,17).lunar.toString()}")
            Button(
                onClick = {
                    navController.navigate(Route.Countdown.route)
                }
            ) {
                Text("添加倒计时")
            }
//
        LazyColumn {
            items(list) { model ->
                CountdownItem(model) {
                    navController.navigate("Countdown?model=${Uri.encode(Gson().toJson(model))}"
                    )
                }
            }
        }
        }
    }
}
