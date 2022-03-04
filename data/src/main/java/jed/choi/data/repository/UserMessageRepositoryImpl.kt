package jed.choi.data.repository

import jed.choi.data.mapper.toUserMessageData
import jed.choi.data.mapper.toUserMessageEntity
import jed.choi.data.model.UserMessageData
import jed.choi.domain.entity.UserMessageEntity
import jed.choi.domain.repository.UserMessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMessageRepositoryImpl @Inject constructor(
    private val localDataSource: UserMessageLocalDataSource
) : UserMessageRepository {
    override fun getOldestUserMessage(): Flow<UserMessageEntity?> = localDataSource.getOldestUserMessage().map { it?.toUserMessageEntity() }

    override suspend fun getUserMessages(): List<UserMessageEntity> {
        return localDataSource.getUserMessages().map { it.toUserMessageEntity() }
    }

    override suspend fun saveUserMessage(userMessage: UserMessageEntity) {
        localDataSource.saveUserMessage(userMessage.toUserMessageData())
    }

    override suspend fun removeUserMessage(id: Long) {
        localDataSource.removeUserMessage(id)
    }

    override suspend fun addUserMessage(message: String) {
        localDataSource.saveUserMessage(UserMessageData(message = message))
    }
}