package jed.choi.domain.repository

import jed.choi.domain.entity.UserMessageEntity

interface UserMessageRepository {
    suspend fun getUserMessage(): List<UserMessageEntity>
    suspend fun saveUserMessage(userMessages: List<UserMessageEntity>)
}