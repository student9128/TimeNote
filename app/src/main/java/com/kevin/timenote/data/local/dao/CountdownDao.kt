package com.kevin.timenote.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kevin.timenote.data.local.entity.CountdownEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountdownDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(entity: CountdownEntity)

    @Delete
    suspend fun delete(entity: CountdownEntity)

    @Update
    suspend fun update(entity: CountdownEntity)

    @Query("SELECT * FROM countdown ORDER BY startTime ASC")
    fun observeAll(): Flow<List<CountdownEntity>>
}