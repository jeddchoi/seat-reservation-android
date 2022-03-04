package jed.choi.domain.repository

import jed.choi.domain.entity.UserMessageEntity
import kotlinx.coroutines.flow.Flow

interface UserMessageRepository {
    fun getOldestUserMessage(): Flow<UserMessageEntity?>
    suspend fun getUserMessages(): List<UserMessageEntity>
    suspend fun saveUserMessage(userMessage: UserMessageEntity)
    suspend fun removeUserMessage(id: Long)
    suspend fun addUserMessage(message: String)
}