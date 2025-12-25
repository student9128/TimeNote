package com.kevin.timenote.data.repository

import com.kevin.timenote.data.local.dao.CountdownDao
import com.kevin.timenote.data.local.entity.CountdownEntity
import com.kevin.timenote.data.mapper.toDomain
import com.kevin.timenote.data.mapper.toEntity
import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.domain.repository.CountdownRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CountdownRepositoryImpl @Inject constructor(private val dao: CountdownDao) :
    CountdownRepository {

    override suspend fun addCountdown(event: CountdownModel) {
        dao.insert(event.toEntity())
    }

    override suspend fun addCountdownAndReturnId(event: CountdownModel): Long {
        return dao.insert(event.toEntity())
    }

    override suspend fun deleteCountdown(event: CountdownModel) {
        dao.delete(event.toEntity())
    }

    override suspend fun updateCountdown(event: CountdownModel) {
        dao.update(event.toEntity())
    }

    override fun getCountdownById(id: Long): Flow<CountdownModel> {
        return dao.getCountdownById(id)
    }

    override fun observeCountdowns(): Flow<List<CountdownModel>> {
        return dao.observeAll()
            .map { list -> list.map { it.toDomain() } }
    }

    override fun observeByDateRange(start: Long, end: Long): Flow<List<CountdownModel>> {
        return dao.observeByDateRange(start, end)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun observePast(time: Long): Flow<List<CountdownModel>> {
        return dao.observePast(time)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun observeFuture(time: Long): Flow<List<CountdownModel>> {
        return dao.observeFuture(time)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun observeUpcoming(
        todayStart: Long,
        limit: Int
    ): Flow<List<CountdownModel>> {
        return dao.observeUpcoming(todayStart, limit).map { list -> list.map { it.toDomain() } }
    }
}
