package com.kevin.timenote.ui.home

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.gson.Gson
import com.kevin.timenote.ui.countdown.CountdownItem
import com.kevin.timenote.base.LocalNavController
import com.kevin.timenote.common.util.TimeL.printD
import com.kevin.timenote.common.util.formatWithPattern
import com.kevin.timenote.ui.navigation.TimeRoute
import com.kevin.timenote.ui.theme.uniformPadding
import com.kevin.timenote.ui.widget.TimeTopBar
import com.nlf.calendar.Solar
import okhttp3.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val list by viewModel.filteredCountdowns.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val date by viewModel.dateLunar.collectAsStateWithLifecycle()
    val dateJieQi by viewModel.dateJieQi.collectAsStateWithLifecycle()
    val dateSolarFestival by viewModel.dateSolarFestival.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshDate()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    val tabs = listOf( "今天","未来", "过去", "全部",)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),

        topBar = {
            TimeTopBar(
                title = "${System.currentTimeMillis().formatWithPattern("yyyy年MM月dd日")}",
                subTitle = "农历 $date",
                festival = dateSolarFestival,
                showBackIcon = false,
                showSearch = true,
                onSearchClick = {})
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(TimeRoute.Countdown()) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    ""
                )
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            PrimaryTabRow(selectedTabIndex = viewModel.selectTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = viewModel.selectTabIndex == index,
                        onClick = {
                            viewModel.updateTab(index)
                        },
                        text = { Text(text = title) }
                    )
                }
            }

            if (list.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(top = uniformPadding, bottom = uniformPadding),
                    verticalArrangement = Arrangement.Top
                ) {
                    items(list) { model ->
                        CountdownItem(model) {
                            navController.navigate(TimeRoute.CountdownDetail(model.id))
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "暂无添加任何事项\n点右下角+快去添加吧",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
