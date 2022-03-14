package jed.choi.domain.usecase

import jed.choi.domain.repository.AuthRepository
import javax.inject.Inject

class GetAuthState @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke() = authRepository.getFirebaseAuthState()
}