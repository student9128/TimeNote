package com.kevin.timenote.ui.countdown

data class CountdownEditUiState(
    val id: Long = 0L,
    val title: String = "",
    val location: String = "",
    val type: String = "",
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
    val date: Long = System.currentTimeMillis(),
    val lunarDate: Long = System.currentTimeMillis(),
    val isLunar: Boolean = false
) {
    val isEditMode: Boolean get() = id != 0L
}