package com.kevin.timenote.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kevin.timenote.data.local.dao.CountdownDao
import com.kevin.timenote.data.local.dao.EventTypeDao
import com.kevin.timenote.data.local.entity.CountdownEntity
import com.kevin.timenote.data.local.entity.EventType

@Database(
    entities = [CountdownEntity::class, EventType::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class TimeAppDatabase : RoomDatabase() {
    abstract fun countdownDao(): CountdownDao
    abstract fun eventTypeDao(): EventTypeDao
}
