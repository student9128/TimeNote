package com.kevin.timenote

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.kevin.timenote.ui.main.MainScreen
import com.kevin.timenote.ui.theme.TimeNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimeNoteTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    MainScreen(navController, modifier = Modifier.padding(innerPadding))
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