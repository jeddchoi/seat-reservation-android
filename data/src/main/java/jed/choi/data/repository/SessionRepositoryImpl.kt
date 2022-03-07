package jed.choi.data.repository

import jed.choi.domain.UserSession
import jed.choi.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor(
) : SessionRepository {
    private val _userSessionTest = MutableStateFlow(UserSession())
    private val userSession: StateFlow<UserSession>
        get() = _userSessionTest

    override fun updateUserSession(session: UserSession) {
        _userSessionTest.update { session }
    }

    override fun getUserSession(): Flow<UserSession> = userSession
}