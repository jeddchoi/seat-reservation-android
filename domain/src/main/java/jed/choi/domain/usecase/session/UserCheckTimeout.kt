package jed.choi.domain.usecase.session

import jed.choi.domain.UNEXPECTED_ERROR_MESSAGE
import jed.choi.domain.UserState
import jed.choi.domain.entity.Response
import jed.choi.domain.repository.SessionRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserCheckTimeout @Inject constructor(
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke() =
        flow {
            try {
                emit(Response.Loading)

                val session = sessionRepository.getUserSession()
                when (session?.userState) {
                    UserState.NEED_USER_CHECK -> {
                        if (sessionRepository.onUserCheckTimeout()) {
                            emit(Response.Success(true))
                        }
                    }
                    else -> throw RuntimeException("Can't perform when user state is ${session?.userState}")
                }
            } catch (e: Exception) {
                emit(Response.Failure(e.message ?: UNEXPECTED_ERROR_MESSAGE))
            }
        }
}