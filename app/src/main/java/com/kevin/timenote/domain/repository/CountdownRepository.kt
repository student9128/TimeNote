package com.kevin.timenote.domain.repository

import com.kevin.timenote.domain.model.CountdownModel
import kotlinx.coroutines.flow.Flow

interface CountdownRepository {

    suspend fun addCountdown(event: CountdownModel)

    suspend fun deleteCountdown(event: CountdownModel)

    suspend fun updateCountdown(event: CountdownModel)

    fun observeCountdowns(): Flow<List<CountdownModel>>
}