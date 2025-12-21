package com.kevin.timenote.ui.mine

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kevin.timenote.MainViewModel
import com.kevin.timenote.domain.model.ThemeMode
import com.kevin.timenote.ui.theme.uniformPadding
import com.kevin.timenote.ui.widget.TimeTopBar

@SuppressLint("ContextCastToActivity")
@Composable
fun MineScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val activity = remember(context) { context as? ComponentActivity }
    val currentThemeMode by mainViewModel.themeMode.collectAsStateWithLifecycle()
    // 拦截返回键
    BackHandler(enabled = true) {
        // 直接关闭当前 Activity，即退出 App
        activity?.finish()
    }
    Scaffold(topBar = { TimeTopBar(title = "我的", showBackIcon = false) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(uniformPadding)
        ) {
            // 标题区
            Text(
                text = "主题设置",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 3. 构建三个选项卡
            ThemeOptionItem(
                title = "动态颜色 (跟随系统)",
                selected = currentThemeMode == ThemeMode.Dynamic,
                onClick = { mainViewModel.updateTheme(ThemeMode.Dynamic) }
            )

            ThemeOptionItem(
                title = "浅色模式",
                selected = currentThemeMode == ThemeMode.Light,
                onClick = { mainViewModel.updateTheme(ThemeMode.Light) }
            )

            ThemeOptionItem(
                title = "深色模式",
                selected = currentThemeMode == ThemeMode.Dark,
                onClick = { mainViewModel.updateTheme(ThemeMode.Dark) }
            )
        }
    }
}

@Composable
private fun ThemeOptionItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // 点击整行都可以触发
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick // RadioButton 自身的点击事件
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}