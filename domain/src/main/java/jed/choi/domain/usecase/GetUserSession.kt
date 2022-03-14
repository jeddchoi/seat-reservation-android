package jed.choi.domain.usecase

import jed.choi.domain.repository.SessionRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserSession @Inject constructor(
    private val getAuthState: GetAuthState,
//    private val seatRepository: SeatRepository,
    private val sessionRepository: SessionRepository,
) {
    operator fun invoke() = getAuthState.invoke().map {
    }

//    operator fun invoke(): Flow<Response<MyUserStateEntity>> = combine(
//        authRepository.getLoggedInUser(),
//        sessionRepository.getUserSession()
//    ) { userEntity, userSession ->
//        when (userEntity) {
//            is Response.Failure -> Response.Failure(userEntity.errorMessage)
//            Response.Loading -> Response.Loading
//            is Response.Success -> Response.Success(
//                MyUserStateEntity(
//                    myName = userEntity.data?.name,
//                    mySession = userSession
//                )
//            )
//        }
//
//    }
}