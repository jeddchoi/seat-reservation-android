package jed.choi.seatreservation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jed.choi.data.repository.SessionRepositoryImpl
import jed.choi.data.repository.UserMessageRepositoryImpl
import jed.choi.domain.repository.SessionRepository
import jed.choi.domain.repository.UserMessageRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsUserMessageRepository(
        userRepositoryImpl: UserMessageRepositoryImpl
    ): UserMessageRepository

    @Binds
    abstract fun bindsSessionRepository(
        sessionRepositoryImpl: SessionRepositoryImpl
    ): SessionRepository
}
