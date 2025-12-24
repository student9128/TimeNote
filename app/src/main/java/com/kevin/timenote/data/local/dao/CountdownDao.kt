package com.kevin.timenote.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kevin.timenote.data.local.entity.CountdownEntity
import com.kevin.timenote.domain.model.CountdownModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CountdownDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(entity: CountdownEntity): Long

    @Delete
    suspend fun delete(entity: CountdownEntity)

    @Update
    suspend fun update(entity: CountdownEntity)

    @Query("SELECT * FROM countdown ORDER BY date ASC")
    fun observeAll(): Flow<List<CountdownEntity>>

    @Query("SELECT * FROM countdown WHERE id = :id")
    fun getCountdownById(id: Long): Flow<CountdownModel>

    @Query("SELECT * FROM countdown WHERE date >= :start AND date <= :end ORDER BY date ASC")
    fun observeByDateRange(start: Long, end: Long): Flow<List<CountdownEntity>>

    @Query("SELECT * FROM countdown WHERE date < :time ORDER BY date DESC")
    fun observePast(time: Long): Flow<List<CountdownEntity>>

    @Query("SELECT * FROM countdown WHERE date > :time ORDER BY date ASC")
    fun observeFuture(time: Long): Flow<List<CountdownEntity>>
}
