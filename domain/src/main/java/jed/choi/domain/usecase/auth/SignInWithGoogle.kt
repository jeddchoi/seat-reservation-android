package jed.choi.domain.usecase.auth

import jed.choi.domain.entity.Response
import jed.choi.domain.repository.AuthRepository
import jed.choi.domain.repository.SessionRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class SignInWithGoogle @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(idToken: String) =
        authRepository.signInWithGoogle(idToken).transform { response ->
            if (response is Response.Success) {
                val isNewUser = response.data
                if (isNewUser) {
                    emitAll(authRepository.createUserInRealtimeDatabase().transform {
                        if (it is Response.Success) {
                            emitAll(sessionRepository.createUserSessionInRealtimeDatabase(it.data))
                        }
                    })
                }
                emit(Response.Success(true))
            } else {
                emit(response)
            }
        }
}