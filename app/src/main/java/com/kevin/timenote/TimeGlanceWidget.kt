package com.kevin.timenote

import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import androidx.glance.text.Text
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.ActionCallback
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceModifier
import androidx.glance.layout.Alignment
import androidx.glance.layout.fillMaxSize
import androidx.glance.action.ActionParameters
import androidx.glance.background
import androidx.glance.layout.padding
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition

val COUNT_KEY = intPreferencesKey("widget_count")
class TimeGlanceWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition
    // 核心方法：定义小组件的内容
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // 设置内容和主题
        provideContent {
            val prefs = currentState<androidx.datastore.preferences.core.Preferences>()
            // 读取 COUNT_KEY 存储的值，如果没有则默认为 0
            val currentCount = prefs[COUNT_KEY] ?: 0

            GlanceContent(currentCount) // 将当前计数传入 Composable
        }
    }
}

@Composable
fun GlanceContent(counterValue: Int) {
    // 1. 获取当前的状态 (这里我们使用一个硬编码的示例计数)

    // 2. 布局和视图定义 (类似于 Jetpack Compose)
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground() // 必须添加，用于设置小组件的背景和圆角
            .background(R.color.white) // 自定义背景色
            .padding(16.dp),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        Text(
            text = "当前计数: $counterValue",
            modifier = GlanceModifier.padding(bottom = 8.dp)
        )

        // 按钮：设置一个点击动作，执行回调函数
        Text(
            text = "点击刷新",
            modifier = GlanceModifier
                .background(R.color.teal_200)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickable(
                    // 关键：定义一个点击动作，运行下面的 UpdateActionCallback
                    actionRunCallback<UpdateActionCallback>()
                )
        )
    }
}

/**
 * 这是一个 Glace 动作回调，用于处理按钮点击事件并更新小组件。
 * 必须是一个顶级类或静态内部类。
 */
class UpdateActionCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // 1. 在这里执行你的业务逻辑（例如，从网络获取新数据，或增加计数器）
        // 关键：使用 updateAppWidgetState 安全地更新状态
        updateAppWidgetState(context, glanceId) { prefs ->
            // 1. 读取旧值，如果没有则默认为 0
            val currentCount = prefs[COUNT_KEY] ?: 0

            // 2. 计算新值 (例如，加 1)
            prefs[COUNT_KEY] = currentCount + 1
        }

        // 3. 通知 Glance 重绘小组件以显示新状态
        // 必须调用这个方法，否则界面不会更新！
        TimeGlanceWidget().update(context, glanceId)

        // 2. 强制 Glace 重绘小组件以显示变化 (虽然这个例子中计数器是硬编码的)
        // ExampleGlanceWidget().update(context, glanceId)
    }
}