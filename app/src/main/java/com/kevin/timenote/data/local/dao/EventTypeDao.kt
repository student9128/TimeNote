package com.kevin.timenote.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevin.timenote.data.local.entity.EventType
import kotlinx.coroutines.flow.Flow

@Dao
interface EventTypeDao {
    @Query("SELECT * FROM event_types ORDER BY id ASC")
    fun getAllTypes(): Flow<List<EventType>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertType(eventType: EventType)

    @Delete
    suspend fun deleteType(eventType: EventType)

    // 初始化默认数据用
    @Query("SELECT COUNT(*) FROM event_types")
    suspend fun getCount(): Int
}