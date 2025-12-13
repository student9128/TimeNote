package com.kevin.timenote.di

import com.kevin.timenote.data.local.dao.CountdownDao
import com.kevin.timenote.data.repository.CountdownRepositoryImpl
import com.kevin.timenote.domain.repository.CountdownRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCountdownRepository(
        dao: CountdownDao
    ): CountdownRepository =
        CountdownRepositoryImpl(dao)
}
