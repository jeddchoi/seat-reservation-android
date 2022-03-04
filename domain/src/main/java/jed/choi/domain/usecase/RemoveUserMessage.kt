package jed.choi.domain.usecase

import jed.choi.domain.repository.UserMessageRepository
import javax.inject.Inject

class RemoveUserMessage @Inject constructor(
    private val userMessageRepository: UserMessageRepository,
) {
    suspend operator fun invoke(id: Long) {
        userMessageRepository.removeUserMessage(id)
    }
}