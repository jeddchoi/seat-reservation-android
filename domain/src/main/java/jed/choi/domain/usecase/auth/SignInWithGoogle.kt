package jed.choi.domain.usecase.auth

import jed.choi.domain.UNEXPECTED_ERROR_MESSAGE
import jed.choi.domain.entity.Response
import jed.choi.domain.repository.AuthRepository
import jed.choi.domain.repository.SessionRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInWithGoogle @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(idToken: String) = flow {
        try {
            emit(Response.Loading)
            // new user
            if (authRepository.signInWithGoogle(idToken) && !authRepository.createUserInFirestore()) {
                throw RuntimeException("sign in with google and create user in firestore failed")
            }
            if (!sessionRepository.onSignInUser()) {
                throw RuntimeException("session update on sign in user failed")
            }
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: UNEXPECTED_ERROR_MESSAGE))
        }
    }
}