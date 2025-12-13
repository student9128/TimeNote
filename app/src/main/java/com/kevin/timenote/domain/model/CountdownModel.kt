package com.kevin.timenote.domain.model

data class CountdownModel(
    val id: Long = 0,
    val title: String,
    val location: String,
    val type: String,
    val startTime: Long,
    val endTime: Long
)
