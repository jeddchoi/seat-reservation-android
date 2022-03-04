package jed.choi.seatreservation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jed.choi.data.repository.UserMessageLocalDataSource
import jed.choi.local.source.UserMessageLocalDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindUserMessageLocalDataSource(
        userMessageLocalDatSourceImpl: UserMessageLocalDataSourceImpl
    ): UserMessageLocalDataSource
}