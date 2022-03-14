package jed.choi.domain.usecase.session

import jed.choi.domain.repository.SessionRepository
import jed.choi.domain.usecase.auth.GetAuthState
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetUserSession @Inject constructor(
    private val getAuthState: GetAuthState,
//    private val seatRepository: SeatRepository,
    private val sessionRepository: SessionRepository,
) {
    operator fun invoke() = getAuthState.invoke().transform {
        if (it != null) {
            emitAll(sessionRepository.getUserSession(it))
        }
    }
}