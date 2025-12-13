package com.kevin.timenote.data.repository

import com.kevin.timenote.data.local.dao.CountdownDao
import com.kevin.timenote.data.mapper.toDomain
import com.kevin.timenote.data.mapper.toEntity
import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.domain.repository.CountdownRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CountdownRepositoryImpl(private val dao: CountdownDao) : CountdownRepository {

    override suspend fun addCountdown(event: CountdownModel) {
        dao.insert(event.toEntity())
    }

    override suspend fun deleteCountdown(event: CountdownModel) {
        dao.delete(event.toEntity())
    }

    override suspend fun updateCountdown(event: CountdownModel) {
        dao.update(event.toEntity())
    }

    override fun observeCountdowns(): Flow<List<CountdownModel>> {
        return dao.observeAll()
            .map { list -> list.map { it.toDomain() } }
    }
}