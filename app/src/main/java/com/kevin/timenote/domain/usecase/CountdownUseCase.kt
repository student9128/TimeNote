package com.kevin.timenote.domain.usecase

import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.domain.repository.CountdownRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//这里可以使用@Inject constructor 于 UseCaseModule.kt两个两者取其一就可，
// 目的是告诉 Hilt CountdownUseCase需要一个CountdownRepository
class CountdownUseCase @Inject constructor(
    private val repository: CountdownRepository
) {

    fun observeCountdowns() =
        repository.observeCountdowns()

    fun observeByDateRange(start: Long, end: Long): Flow<List<CountdownModel>> =
        repository.observeByDateRange(start, end)

    fun observePast(time: Long): Flow<List<CountdownModel>> =
        repository.observePast(time)

    fun observeFuture(time: Long): Flow<List<CountdownModel>> =
        repository.observeFuture(time)

    suspend fun save(model: CountdownModel) {
        if (model.id == 0L) {
            repository.addCountdown(model)
        } else {
            repository.updateCountdown(model)
        }
    }

    suspend fun add(model: CountdownModel) =
        repository.addCountdown(model)

    suspend fun update(model: CountdownModel) =
        repository.updateCountdown(model)

    suspend fun delete(model: CountdownModel) =
        repository.deleteCountdown(model)

    fun getCountdownById(id: Long)=repository.getCountdownById(id)

}
