package jed.choi.domain.usecase

import jed.choi.domain.entity.MyUserStateEntity
import jed.choi.domain.usecase.auth.ObserveUser
import jed.choi.domain.usecase.seat.ObserveUserSeat
import jed.choi.domain.usecase.session.ObserveUserSession
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveUserState @Inject constructor(
    private val observeUser: ObserveUser,
    private val observeUserSession: ObserveUserSession,
    private val observeUserSeat: ObserveUserSeat,
) {
    operator fun invoke() = combine(
        observeUser.invoke(),
        observeUserSeat.invoke(),
        observeUserSession.invoke()
    ) { user, seat, userSession ->
        MyUserStateEntity(user = user, mySeat = seat, mySession = userSession)
    }
}