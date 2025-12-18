package com.kevin.timenote

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.kevin.timenote.base.LocalNavController
import com.kevin.timenote.base.LocalToast
import com.kevin.timenote.base.UIActions
import com.kevin.timenote.ui.widget.LoadingWidget
import kotlinx.coroutines.launch


@Composable
fun AppProviders(content: @Composable () -> Unit) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var loadingTips by remember { mutableStateOf("正在加载...") }
    val uiActions = remember(scope) {
        object : UIActions {
            override fun showToast(message: String) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            override fun showSnackBar(message: String) {
                scope.launch {
                    snackBarHostState.showSnackbar(message)
                }
            }

            override fun showLoading(show: Boolean, loadingContent: String) {
                isLoading = show
                loadingTips = loadingContent
            }
        }
    }
    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalToast provides uiActions
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content() // 渲染主业务页面
            // 全局 Loading 蒙层
            if (isLoading) {
                LoadingWidget(loadingTips)
            }
        }
    }
}