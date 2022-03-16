package jed.choi.domain.usecase.auth

import jed.choi.domain.repository.AuthRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class ObserveUser @Inject constructor(
    private val authRepository: AuthRepository,
) {
    // if user auth state changes, emitAll() should be cancelled
    operator fun invoke() = authRepository.observeAuthUid().transformLatest {
        if (it != null) emitAll(authRepository.observeUser(it))
        else emit(null)
    }
}