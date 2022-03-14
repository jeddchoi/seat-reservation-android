package jed.choi.domain.usecase

import jed.choi.domain.repository.AuthRepository
import javax.inject.Inject

class IsUserAuthenticated @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke() = authRepository.isUserAuthenticatedInFirebase()
}