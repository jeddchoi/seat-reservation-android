package jed.choi.domain.usecase.auth

import jed.choi.domain.UNEXPECTED_ERROR_MESSAGE
import jed.choi.domain.entity.Response
import jed.choi.domain.repository.AuthRepository
import jed.choi.domain.repository.SessionRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignOut @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke() = flow {
        try {
            emit(Response.Loading)
            if (sessionRepository.onSignOutUser()) {
                authRepository.signOut()
                emit(Response.Success(true))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: UNEXPECTED_ERROR_MESSAGE))
        }
    }
}