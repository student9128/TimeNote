package com.kevin.timenote.ui.home

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.kevin.timenote.ui.countdown.CountdownItem
import com.kevin.timenote.ui.navigation.Route

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val list by viewModel.countdowns.collectAsState()

    Column {
        Button(
            onClick = {
                navController.navigate(Route.Countdown.route)
            }
        ) {
            Text("添加倒计时")
        }

        LazyColumn {
            items(list) { model ->
                CountdownItem(model) {
                    navController.navigate(
                        Route.Countdown.route +
                                "?model=${Uri.encode(Gson().toJson(model))}"
                    )
                }
            }
        }
    }
}
