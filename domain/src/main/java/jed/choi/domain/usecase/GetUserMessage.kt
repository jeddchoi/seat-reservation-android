package jed.choi.domain.usecase

import jed.choi.domain.entity.UserMessageEntity
import jed.choi.domain.repository.UserMessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserMessage @Inject constructor(
    private val userMessageRepository: UserMessageRepository,
) {
    operator fun invoke(): Flow<UserMessageEntity?> =
        userMessageRepository.getOldestUserMessage().flowOn(
            Dispatchers.IO
        )
}