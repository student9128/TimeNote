package com.kevin.timenote.data.local.entity

import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_types")
data class EventType(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: Long,
    val isSystemDefault: Boolean=false
)
