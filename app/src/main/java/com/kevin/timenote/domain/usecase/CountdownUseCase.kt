package com.kevin.timenote.domain.usecase

import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.domain.repository.CountdownRepository
import javax.inject.Inject

class CountdownUseCase @Inject constructor(
    private val repository: CountdownRepository
) {

    fun observeCountdowns() =
        repository.observeCountdowns()

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

}
