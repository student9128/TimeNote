package com.kevin.timenote

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.action.ActionCallback
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.layout.Alignment
import androidx.glance.layout.fillMaxSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.background
import androidx.glance.layout.padding
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.kevin.timenote.common.util.daysUntilTarget
import com.kevin.timenote.common.util.formatWithPattern
import com.kevin.timenote.common.util.getStartOfDay
import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.ui.countdown.Te
import com.kevin.timenote.ui.theme.PrimaryLight
import com.kevin.timenote.ui.theme.uniformPadding
import com.nlf.calendar.Lunar
import dagger.hilt.EntryPoints
import java.util.Date
import kotlin.math.abs
import kotlin.time.ExperimentalTime

val COUNT_KEY = intPreferencesKey("widget_count")

class TimeGlanceWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    // 核心方法：定义小组件的内容
    @SuppressLint("ProduceStateDoesNotAssignValue")
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val applicationContext = context.applicationContext
        val entryPoint = EntryPoints.get(applicationContext, WidgetEntryPoint::class.java)
        val repository = entryPoint.countdownRepository()
        // 设置内容和主题
        provideContent {
            val now = System.currentTimeMillis()
            val countdowns by produceState<List<CountdownModel>>(initialValue = emptyList<CountdownModel>()) {
                repository.observeUpcoming(now.getStartOfDay())
                    .collect { newList -> value = newList }
            }
            val prefs = currentState<androidx.datastore.preferences.core.Preferences>()
            // 读取 COUNT_KEY 存储的值，如果没有则默认为 0
            val currentCount = prefs[COUNT_KEY] ?: 0

            GlanceTheme() {
                GlanceContent(countdowns) // 将当前计数传入 Composable
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun GlanceContent(dataList: List<CountdownModel>) {
    // 1. 获取当前的状态 (这里我们使用一个硬编码的示例计数)

    val now = System.currentTimeMillis()
    val lunar = Lunar.fromDate(Date(now))
    val jieQi = lunar.jieQi
    val context = LocalContext.current
    // 2. 布局和视图定义 (类似于 Jetpack Compose)
    Column(modifier = GlanceModifier.fillMaxSize().background(GlanceTheme.colors.background)) {
        Column(
            modifier = GlanceModifier.fillMaxWidth().padding(10.dp)
                .clickable(actionStartActivity<MainActivity>())
        ) {
            Text(
                now.formatWithPattern("yyyy年MM月dd日"),
                style = TextStyle(color = GlanceTheme.colors.primary, fontSize = 18.sp)
            )
            if (jieQi.isEmpty()) {
                Text(
                    "农历 ${lunar.toString()} 星期${lunar.weekInChinese}",
                    style = TextStyle(color = GlanceTheme.colors.primary, fontSize = 12.sp)
                )
            } else {
                Text(
                    "农历 ${lunar.toString()} $jieQi 星期${lunar.weekInChinese}",
                    style = TextStyle(color = GlanceTheme.colors.primary, fontSize = 12.sp)
                )
            }
        }

        LazyColumn(

        ) {
            items(dataList) { model ->
                val days = model.date.daysUntilTarget()
                val uri = Uri.parse("timenote://detail/${model.id}")
                val action = actionStartActivity(
                    Intent(Intent.ACTION_VIEW, uri).apply {
                        `package` = context.packageName
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                Row(
                    modifier = GlanceModifier.fillMaxSize().padding(
                        vertical = 10.dp,
                        horizontal = uniformPadding
                    ).background(day = Color.White, night = Color.Black).clickable(action),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = GlanceModifier.size(8.dp)
                            .background(Color(model.eventTypeColor)).cornerRadius(4.dp)
                    ) { }
                    if (days > 0) {
                        Row(modifier = GlanceModifier.padding(start = 10.dp)) {
                            Text("距离", style = TextStyle(color = GlanceTheme.colors.onSurface))
                            Text(
                                " ${model.title} ",
                                style = TextStyle(
                                    color = GlanceTheme.colors.primary,
                                    fontStyle = FontStyle.Italic
                                )
                            )
                            Text("还有", style = TextStyle(color = GlanceTheme.colors.onSurface))
                            Text(
                                " $days ", style = TextStyle(
                                    color = GlanceTheme.colors.primary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text("天", style = TextStyle(color = GlanceTheme.colors.onSurface))
                        }
                    } else {
                        Row(modifier = GlanceModifier.padding(start = 10.dp)) {
                            Text(
                                model.title,
                                style = TextStyle(
                                    color = GlanceTheme.colors.primary,
                                    fontStyle = FontStyle.Italic
                                )
                            )
                            Text(
                                " 已经过去",
                                style = TextStyle(color = GlanceTheme.colors.onSurface)
                            )
                            Text(
                                " ${abs(days)} ", style = TextStyle(
                                    color = GlanceTheme.colors.primary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text("天", style = TextStyle(color = GlanceTheme.colors.onSurface))
                        }

                    }
                }
            }
        }

    }
//    LazyColumn() {
//        items(dataList){model->
//            val days = model.date.daysUntilTarget()
//            Text(
//                "${processEventTitle(days, model.title, model.eventTypeName)}",
//                modifier = GlanceModifier.padding(vertical = 10.dp)
//            )
//        }
//    }
//    Column(
//        modifier = GlanceModifier.fillMaxSize().appWidgetBackground() // 必须添加，用于设置小组件的背景和圆角
//            .background(R.color.white) // 自定义背景色
//            .padding(16.dp),
//        horizontalAlignment = Alignment.Horizontal.Start,
//        verticalAlignment = Alignment.Vertical.CenterVertically
//    ) {
//        Row() {
//            IconButton(onClick = { actionRunCallback<UpdateActionCallback>() }) {
//                Icon(
//                    imageVector = Icons.Default.Refresh,
//                    contentDescription = "Localized description"
//                )
//            }
//        }
//        list.forEach { model ->
//
//        }
//        Text(
//            text = "当前计数: $counterValue", modifier = GlanceModifier.padding(bottom = 8.dp)
//        )
//
//        // 按钮：设置一个点击动作，执行回调函数
//        Text(
//            text = "点击刷新",
//            modifier = GlanceModifier.background(R.color.teal_200)
//                .padding(horizontal = 12.dp, vertical = 8.dp).clickable(
//                    // 关键：定义一个点击动作，运行下面的 UpdateActionCallback
//                    actionRunCallback<UpdateActionCallback>()
//                )
//        )
//    }
}


/**
 * 这是一个 Glace 动作回调，用于处理按钮点击事件并更新小组件。
 * 必须是一个顶级类或静态内部类。
 */
class UpdateActionCallback : ActionCallback {

    override suspend fun onAction(
        context: Context, glanceId: GlanceId, parameters: ActionParameters
    ) {
        // 1. 在这里执行你的业务逻辑（例如，从网络获取新数据，或增加计数器）
        // 关键：使用 updateAppWidgetState 安全地更新状态
//        updateAppWidgetState(context, glanceId) { prefs ->
//            // 1. 读取旧值，如果没有则默认为 0
//            val currentCount = prefs[COUNT_KEY] ?: 0
//
//            // 2. 计算新值 (例如，加 1)
//            prefs[COUNT_KEY] = currentCount + 1
//        }
        val repository = EntryPoints.get(
            context.applicationContext, WidgetEntryPoint::class.java
        ).countdownRepository()


        // 3. 通知 Glance 重绘小组件以显示新状态
        // 必须调用这个方法，否则界面不会更新！
        TimeGlanceWidget().update(context, glanceId)

        // 2. 强制 Glace 重绘小组件以显示变化 (虽然这个例子中计数器是硬编码的)
        // ExampleGlanceWidget().update(context, glanceId)
    }
}