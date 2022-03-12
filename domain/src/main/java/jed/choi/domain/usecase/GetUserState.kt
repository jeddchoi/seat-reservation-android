package jed.choi.domain.usecase

import jed.choi.domain.entity.MyUserStateEntity
import jed.choi.domain.repository.AuthRepository
import jed.choi.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetUserState @Inject constructor(
    private val authRepository: AuthRepository,
//    private val seatRepository: SeatRepository,
    private val sessionRepository: SessionRepository,
) {
    operator fun invoke(): Flow<MyUserStateEntity> = combine(
        authRepository.getLoggedInUser(),
        sessionRepository.getUserSession()
    ) { userEntity, userSession ->
        MyUserStateEntity(myName = userEntity.name, mySession = userSession)
    }
}