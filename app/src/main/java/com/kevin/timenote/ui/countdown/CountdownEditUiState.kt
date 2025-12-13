package com.kevin.timenote.ui.countdown

data class CountdownEditUiState(
    val id: Long = 0L,
    val title: String = "",
    val location: String = "",
    val type: String = "",
    val startTime: Long = 0L,
    val endTime: Long = 0L
) {
    val isEditMode: Boolean get() = id != 0L
}