package com.kevin.timenote.ui.countdown

import com.kevin.timenote.ui.theme.eventDefaultColor

data class CountdownEditUiState(
    val id: Long = 0L,
    val title: String = "",
    val location: String = "",
    val type: String = "",
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
    val date: Long = System.currentTimeMillis(),
    val lunarDate: Long = System.currentTimeMillis(),
    val isLunar: Boolean = false,
    val eventTypeName: String = "倒数日", // 事件类型名称，提供默认值
    val eventTypeColor: Long = eventDefaultColor
) {
    val isEditMode: Boolean get() = id != 0L
}