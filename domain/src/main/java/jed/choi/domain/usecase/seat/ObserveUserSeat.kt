package jed.choi.domain.usecase.seat

import jed.choi.domain.repository.SeatRepository
import jed.choi.domain.usecase.auth.ObserveAuthUid
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class ObserveUserSeat @Inject constructor(
    private val observeAuthUid: ObserveAuthUid,
    private val seatRepository: SeatRepository
) {
    // if user auth state changes, emitAll() should be cancelled
    operator fun invoke() = observeAuthUid.invoke().transformLatest {
        if (it != null) emitAll(seatRepository.observeUserSeat(it))
        else emit(null)
    }
}