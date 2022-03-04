package jed.choi.data.repository

import jed.choi.data.model.UserMessageData
import kotlinx.coroutines.flow.Flow

interface UserMessageLocalDataSource {
    fun getOldestUserMessage(): Flow<UserMessageData?>
    suspend fun getUserMessages(): List<UserMessageData>
    suspend fun saveUserMessage(userMessage: UserMessageData)
    suspend fun removeUserMessage(id: Long)
}