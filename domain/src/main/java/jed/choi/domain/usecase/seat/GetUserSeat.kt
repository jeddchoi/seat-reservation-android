package jed.choi.domain.usecase.seat

import jed.choi.domain.repository.SeatRepository
import javax.inject.Inject

class GetUserSeat @Inject constructor(
    private val seatRepository: SeatRepository
) {
    suspend operator fun invoke() = seatRepository.getUserSeat()
}