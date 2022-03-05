package jed.choi.domain.usecase

import jed.choi.domain.entity.MyUserStateEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserState @Inject constructor(
//    private val authRepository: AuthRepository,
//    private val seatRepository: SeatRepository,
//    private val sessionRepository: SessionRepository,
) {
    operator fun invoke(): Flow<MyUserStateEntity?> = TODO()
}