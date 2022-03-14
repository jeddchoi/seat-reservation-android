package jed.choi.domain.usecase.auth

import jed.choi.domain.entity.Response
import jed.choi.domain.repository.AuthRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class SignInWithGoogle @Inject constructor(
    private val authRepository: AuthRepository,
){
    suspend operator fun invoke(idToken: String) = authRepository.signInWithGoogle(idToken).transform { response ->
        if (response is Response.Success) {
            val isNewUser = response.data
            if (isNewUser) {
                emitAll(authRepository.createUserInRealtimeDatabase())
            }
            emit(Response.Success(true))
        } else {
            emit(response)
        }
    }
}