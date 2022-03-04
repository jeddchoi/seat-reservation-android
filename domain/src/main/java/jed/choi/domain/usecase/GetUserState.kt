package jed.choi.domain.usecase

import jed.choi.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserState @Inject constructor(
    private val authRepository: AuthRepository
) {
//    operator fun invoke(): Flow<UserMessageEntity?> =
//        userMessageRepository.getOldestUserMessage().flowOn(
//            Dispatchers.IO
//        )
}