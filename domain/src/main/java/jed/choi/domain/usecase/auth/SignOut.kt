package jed.choi.domain.usecase.auth

import jed.choi.domain.repository.AuthRepository
import javax.inject.Inject

class SignOut @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.signOut()
}