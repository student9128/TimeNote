package com.kevin.timenote.domain.model

import com.kevin.timenote.ui.theme.eventDefaultColor

data class CountdownModel(
    val id: Long = 0,
    val title: String,
    val location: String,
    val type: String,
    val startTime: Long,
    val endTime: Long,
    val date: Long = System.currentTimeMillis(),
    val lunarDate: String,
    val isLunar: Boolean = false,
    val eventTypeName: String = "倒数日", // 事件类型名称，提供默认值
    val eventTypeColor: Long = eventDefaultColor,
    val repeatMode: RepeatMode = RepeatMode.NONE
)
