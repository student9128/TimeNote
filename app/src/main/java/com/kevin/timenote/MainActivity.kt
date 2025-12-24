package com.kevin.timenote

import android.app.ComponentCaller
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kevin.timenote.ui.navigation.AppNavHost
import com.kevin.timenote.ui.navigation.Destination
import com.kevin.timenote.domain.model.ThemeMode
import com.kevin.timenote.ui.countdown.CountdownScreen
import com.kevin.timenote.ui.navigation.TimeRoute
import com.kevin.timenote.ui.theme.TimeNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            // 监听主题变化
            val currentThemeMode by mainViewModel.themeMode.collectAsStateWithLifecycle()

            // 根据 Enum 计算 Theme 参数
            val isDarkTheme = when (currentThemeMode) {
                ThemeMode.Light -> false
                ThemeMode.Dark -> true
                ThemeMode.Dynamic -> isSystemInDarkTheme() // 跟随系统
            }

            val isDynamicColor = when (currentThemeMode) {
                ThemeMode.Dynamic -> true // 只有 Dynamic 模式开启 Material You
                else -> false
            }
            TimeNoteTheme(
                darkTheme = isDarkTheme,
                dynamicColor = isDynamicColor) {
                val navController = rememberNavController()
                val startDestination = TimeRoute.Home
                val currentEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentEntry?.destination?.route
                val showBottomBar = currentRoute == TimeRoute.Home::class.qualifiedName ||
                        currentRoute == TimeRoute.Mine::class.qualifiedName
                val bottom: Dp = with(LocalDensity.current) {
                WindowInsets.navigationBars.getBottom(this).toDp()
            }
//                LaunchedEffect("") {
//                    if (intent?.data?.toString() == "timenote://add_event") {
//                        navController.navigate(CountdownScreen()) // Replace with your actual route string for CountdownScreen
//                    }
//                }
                AppProviders(navController) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
                        bottomBar = {
                            AnimatedVisibility(
                                visible = showBottomBar,
                                enter = slideInVertically(initialOffsetY = { it }),
                                exit = slideOutVertically(targetOffsetY = { it })
                            ) {
                                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                                    Destination.entries.forEachIndexed { index, destination ->
                                        NavigationBarItem(
                                            selected = currentRoute == destination.route::class.qualifiedName,
                                            onClick = {
                                                navController.navigate(route = destination.route) {
                                                    // 1. 弹出到导航图的起始目的地，避免栈内堆积重复的 Tab 实例
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState =
                                                            true // 【关键】保存当前切走 Tab 的状态（如滚动位置）
                                                    }

                                                    // 2. 避免在同一个 Tab 上多次点击时产生多个实例
                                                    launchSingleTop = true

                                                    // 3. 重新切回该 Tab 时，自动还原之前的状态
                                                    restoreState = true
                                                }
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
                        },
                       ) { innerPadding ->
                        AppNavHost(
                            navController,
                            startDestination,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
//                CompositionLocalProvider(LocalNavController provides navController) {
//                    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
//                        val currentEntry by navController.currentBackStackEntryAsState()
//                        val currentRoute = currentEntry?.destination?.route
//                        Log.d("currentRoute","currentRoute=${currentRoute}")
//
//                        val tabs = listOf(TimeRoute.Home, TimeRoute.Mine)
//                        val showBottomBar = currentRoute == TimeRoute.Home::class.qualifiedName ||
//                                    currentRoute == TimeRoute.Mine::class.qualifiedName
//                        AnimatedVisibility(visible = showBottomBar,
//                            enter = slideInVertically(initialOffsetY = { it }),
//                            exit = slideOutVertically(targetOffsetY = { it }),) {NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
//                            tabs.forEach { route->
//                                NavigationBarItem(
//                                    selected = currentRoute == route::class.qualifiedName,
//                                    onClick = {
//                                        navController.navigate(route = route) {
//                                            // 1. 弹出到导航图的起始目的地，避免栈内堆积重复的 Tab 实例
//                                            popUpTo(navController.graph.findStartDestination().id) {
//                                                saveState = true // 【关键】保存当前切走 Tab 的状态（如滚动位置）
//                                            }
//
//                                            // 2. 避免在同一个 Tab 上多次点击时产生多个实例
//                                            launchSingleTop = true
//
//                                            // 3. 重新切回该 Tab 时，自动还原之前的状态
//                                            restoreState = true
//                                        }
//                                    },
//                                    icon = {
//                                        Icon(
//                                            imageVector = when (route) {
//                                                TimeRoute.Home  -> Icons.Default.Home
//                                                TimeRoute.Mine -> Icons.Default.Person
//                                                else -> Icons.Default.Home
//                                            },
//                                            contentDescription = null
//                                        )
//                                    },
//                                    label = { Text(  when (route) {
//                                        TimeRoute.Home -> "Home"
//                                        TimeRoute.Mine -> "Mine"
//                                        else -> ""
//                                    }) }
//                                )
//                            }
////                            Destination.entries.forEachIndexed { index, destination ->
////                                NavigationBarItem(
////                                    selected = selectedDestination == destination.route,
////                                    onClick = {
////                                        navController.navigate(route = destination.route) {
////                                            // 1. 弹出到导航图的起始目的地，避免栈内堆积重复的 Tab 实例
////                                            popUpTo(navController.graph.findStartDestination().id) {
////                                                saveState = true // 【关键】保存当前切走 Tab 的状态（如滚动位置）
////                                            }
////
////                                            // 2. 避免在同一个 Tab 上多次点击时产生多个实例
////                                            launchSingleTop = true
////
////                                            // 3. 重新切回该 Tab 时，自动还原之前的状态
////                                            restoreState = true
////                                        }
////                                        selectedDestination = index
////                                    },
////                                    icon = {
////                                        Icon(
////                                            destination.icon,
////                                            contentDescription = destination.contentDescription
////                                        )
////                                    },
////                                    label = { Text(destination.label) }
////                                )
////                            }
//                        } }
//
////                        BottomAppBar() {
////                            Row(
////                                horizontalArrangement = Arrangement.SpaceEvenly,
////                                modifier = Modifier.fillMaxWidth()
////                            ) {
////                                Text("Home", modifier = Modifier.clickable() {
////                                    navController.navigate("Home")
////                                })
////                                Text("Mine", modifier = Modifier.clickable() {
////                                    navController.navigate("Mine")
////                                })
////                            }
////                        }
//                    }) { innerPadding ->
//                        AppNavHost(
//                            navController,
//                            startDestination,
//                            modifier = Modifier.padding(innerPadding)
//                        )
//                    }
//                }
            }
        }
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        setIntent(intent)
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