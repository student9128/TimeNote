package com.kevin.timenote.di

import android.content.Context
import androidx.room.Room
import com.kevin.timenote.data.local.CountdownDatabase
import com.kevin.timenote.data.local.dao.CountdownDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CountdownDatabase =
        Room.databaseBuilder(
            context,
            CountdownDatabase::class.java,
            "countdown.db"
        ).build()

    @Provides
    fun provideDao(db: CountdownDatabase): CountdownDao =
        db.countdownDao()
}
