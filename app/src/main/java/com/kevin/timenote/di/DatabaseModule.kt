package com.kevin.timenote.di

import android.content.Context
import androidx.room.Room
import com.kevin.timenote.data.local.TimeAppDatabase
import com.kevin.timenote.data.local.dao.CountdownDao
import com.kevin.timenote.data.local.dao.EventTypeDao
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
    ): TimeAppDatabase =
        Room.databaseBuilder(
            context,
            TimeAppDatabase::class.java,
            "countdown.db"
        )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideDao(db: TimeAppDatabase): CountdownDao =
        db.countdownDao()

    @Provides
    fun provideEventTypeDao(database: TimeAppDatabase): EventTypeDao {
        return database.eventTypeDao() // The name of the abstract function in your AppDatabase class
    }
}
