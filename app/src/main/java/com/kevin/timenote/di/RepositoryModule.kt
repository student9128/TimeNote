package com.kevin.timenote.di

import com.kevin.timenote.data.local.dao.CountdownDao
import com.kevin.timenote.data.repository.CountdownRepositoryImpl
import com.kevin.timenote.domain.repository.CountdownRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
//用 @Binds的需要用 interface和 impl类 必须是抽象的这一点CountdownRepository正好满足

//@Module
//@InstallIn(SingletonComponent::class)
//object RepositoryModule {
//
//    @Provides
//    @Singleton
//    fun provideCountdownRepository(
//        dao: CountdownDao
//    ): CountdownRepository =
//        CountdownRepositoryImpl(dao)
//
//}
// RepositoryModule
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCountdownRepository(
        impl: CountdownRepositoryImpl
    ): CountdownRepository
}

