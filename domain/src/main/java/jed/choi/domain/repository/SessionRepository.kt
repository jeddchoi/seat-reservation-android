package jed.choi.domain.repository

import jed.choi.domain.UserSession
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getUserSession(): Flow<UserSession>
    fun updateUserSession(session: UserSession)
}