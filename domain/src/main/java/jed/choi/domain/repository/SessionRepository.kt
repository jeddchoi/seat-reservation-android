package jed.choi.domain.repository

import jed.choi.domain.entity.Response
import jed.choi.domain.entity.UserSessionEntity
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getUserSession(uid: String): Flow<UserSessionEntity?>
    suspend fun createUserSessionInRealtimeDatabase(uid: String): Flow<Response<Boolean>>
}