package com.kevin.timenote.ui.home

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.gson.Gson
import com.kevin.timenote.ui.countdown.CountdownItem
import com.kevin.timenote.base.LocalNavController
import com.kevin.timenote.ui.navigation.Countdown
import com.kevin.timenote.ui.widget.TimeTopBar
import com.nlf.calendar.Solar
import okhttp3.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val list by viewModel.countdowns.collectAsState()
    val navController = LocalNavController.current
    val date by viewModel.dateLunar.collectAsStateWithLifecycle()
    LaunchedEffect("") {

    }
//
    Scaffold(topBar = {
        TimeTopBar(title = "", onSearchClick = {})
//        TopAppBar(
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = MaterialTheme.colorScheme.primaryContainer,
//                titleContentColor = MaterialTheme.colorScheme.primary,
//            ),
//            title = {
//                Text("Small Top App Bar")
//            }
//        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = { /* do something */ },
            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
        ) {
            Icon(Icons.Filled.Add, "Add something")
        }
    }) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {
            Text(date)
            Button(
                onClick = {
                    navController.navigate(Countdown)
                }
            ) {
                Text("添加倒计时")
            }
//
            LazyColumn {
                items(list) { model ->
                    CountdownItem(model) {
                        navController.navigate(
                            "Countdown?model=${Uri.encode(Gson().toJson(model))}"
                        )
                    }
                }
            }
        }
    }
}
