package jed.choi.domain.usecase.session

import jed.choi.domain.repository.SessionRepository
import jed.choi.domain.usecase.auth.ObserveAuthUid
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class ObserveUserSession @Inject constructor(
    private val observeAuthUid: ObserveAuthUid,
    private val sessionRepository: SessionRepository,
) {
    // if user auth state changes, emitAll() should be cancelled
    operator fun invoke() = observeAuthUid.invoke().transformLatest {
        if (it != null) emitAll(sessionRepository.observeUserSession(it))
        else emit(null)
    }
}