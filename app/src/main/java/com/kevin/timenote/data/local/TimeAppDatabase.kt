package com.kevin.timenote.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kevin.timenote.data.local.dao.CountdownDao
import com.kevin.timenote.data.local.dao.EventTypeDao
import com.kevin.timenote.data.local.entity.CountdownEntity
import com.kevin.timenote.data.local.entity.EventType

@Database(
    entities = [CountdownEntity::class, EventType::class],
    version = 1
)
abstract class TimeAppDatabase : RoomDatabase() {
    abstract fun countdownDao(): CountdownDao
    abstract fun eventTypeDao(): EventTypeDao
}
