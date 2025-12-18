package com.kevin.timenote

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.kevin.timenote.ui.main.MainScreen
import com.kevin.timenote.ui.navigation.AppNavHost
import com.kevin.timenote.ui.navigation.Destination
import com.kevin.timenote.ui.navigation.LocalNavController
import com.kevin.timenote.ui.navigation.Route
import com.kevin.timenote.ui.theme.TimeNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimeNoteTheme {
                val navController = rememberNavController()
                val startDestination = Destination.Home
                var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
                CompositionLocalProvider(LocalNavController provides navController) {
                    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                        NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                            Destination.entries.forEachIndexed { index, destination ->
                                NavigationBarItem(
                                    selected = selectedDestination == index,
                                    onClick = {
                                        navController.navigate(route = destination.route) {
                                            // 1. 弹出到导航图的起始目的地，避免栈内堆积重复的 Tab 实例
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true // 【关键】保存当前切走 Tab 的状态（如滚动位置）
                                            }

                                            // 2. 避免在同一个 Tab 上多次点击时产生多个实例
                                            launchSingleTop = true

                                            // 3. 重新切回该 Tab 时，自动还原之前的状态
                                            restoreState = true
                                        }
                                        selectedDestination = index
                                    },
                                    icon = {
                                        Icon(
                                            destination.icon,
                                            contentDescription = destination.contentDescription
                                        )
                                    },
                                    label = { Text(destination.label) }
                                )
                            }
                        }
//                        BottomAppBar() {
//                            Row(
//                                horizontalArrangement = Arrangement.SpaceEvenly,
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                Text("Home", modifier = Modifier.clickable() {
//                                    navController.navigate("Home")
//                                })
//                                Text("Mine", modifier = Modifier.clickable() {
//                                    navController.navigate("Mine")
//                                })
//                            }
//                        }
                    }) { innerPadding ->
                        AppNavHost(
                            navController,
                            startDestination,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        Button(onClick = {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }) { Text("测试1") }
        Button(onClick = {
            //只展示自己 Widget
            // 这个方式在很多 ROM 上并不稳定，
            //👉 生产环境一般不推荐
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK).apply {
                putExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_PROVIDER,
                    ComponentName(context, TimeWidgetReceiver::class.java)
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)

        }) { Text("测试2") }
//        Text(
//            text = "打开 App",
//            modifier = GlanceModifier.clickable(
//                actionStartActivity<MainActivity>()
//            )
//        )

    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TimeNoteTheme {
//        Greeting("Android")
//    }
//}