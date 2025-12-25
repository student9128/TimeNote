package com.kevin.timenote

import com.kevin.timenote.domain.repository.CountdownRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun countdownRepository(): CountdownRepository
}