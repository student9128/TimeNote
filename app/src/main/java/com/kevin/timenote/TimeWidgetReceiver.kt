package com.kevin.timenote

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class TimeWidgetReceiver : GlanceAppWidgetReceiver() {
    
    // 2. 关键：在这里实例化您的 Glance UI 类
    // (假设您已创建了一个名为 ExampleGlanceWidget 的类)
    override val glanceAppWidget: GlanceAppWidget = TimeGlanceWidget()
}