package com.kevin.timenote.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countdown")
data class CountdownEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val location: String,
    val type: String,
    val startTime: Long,
    val endTime: Long,
    val date: Long,
    val lunarDate: String,
    val isLunar: Boolean,
    val eventTypeName: String, // 事件类型名称，提供默认值
    val eventTypeColor: Long
)