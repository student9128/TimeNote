package com.kevin.timenote.ui.mine

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kevin.timenote.ui.widget.AppBar

@SuppressLint("ContextCastToActivity")
@Composable
fun MineScreen() {
    val context = LocalContext.current
    val activity = remember(context) { context as? ComponentActivity }

    // 拦截返回键
    BackHandler(enabled = true) {
        // 直接关闭当前 Activity，即退出 App
        activity?.finish()
    }
    Scaffold(topBar = { AppBar(title = "Home", showBackIcon = false) }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Mine")
        }
    }
}