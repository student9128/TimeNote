package com.kevin.timenote.di

import com.kevin.timenote.domain.repository.CountdownRepository
import com.kevin.timenote.domain.usecase.CountdownUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideCountdownUseCase(
        repository: CountdownRepository
    ): CountdownUseCase =
        CountdownUseCase(repository)
}
