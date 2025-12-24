package com.kevin.timenote.data.local

import androidx.room.TypeConverter
import com.kevin.timenote.domain.model.RepeatMode

class Converters {
    @TypeConverter
    fun fromRepeatMode(value: RepeatMode): String {
        return value.name
    }

    @TypeConverter
    fun toRepeatMode(value: String): RepeatMode {
        return try {
            RepeatMode.valueOf(value)
        } catch (e: Exception) {
            RepeatMode.NONE
        }
    }
}
