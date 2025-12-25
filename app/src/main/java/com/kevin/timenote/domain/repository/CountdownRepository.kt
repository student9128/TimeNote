package com.kevin.timenote.domain.repository

import com.kevin.timenote.data.local.entity.CountdownEntity
import com.kevin.timenote.domain.model.CountdownModel
import kotlinx.coroutines.flow.Flow

interface CountdownRepository {

    suspend fun addCountdown(event: CountdownModel)

    suspend fun addCountdownAndReturnId(event: CountdownModel): Long

    suspend fun deleteCountdown(event: CountdownModel)

    suspend fun updateCountdown(event: CountdownModel)

    fun getCountdownById(id: Long): Flow<CountdownModel>

    fun observeCountdowns(): Flow<List<CountdownModel>>

    fun observeByDateRange(start: Long, end: Long): Flow<List<CountdownModel>>

    fun observePast(time: Long): Flow<List<CountdownModel>>

    fun observeFuture(time: Long): Flow<List<CountdownModel>>
    fun observeUpcoming(todayStart: Long, limit: Int = 10): Flow<List<CountdownModel>>
}
