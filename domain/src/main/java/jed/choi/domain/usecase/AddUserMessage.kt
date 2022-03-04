package jed.choi.domain.usecase

import jed.choi.domain.repository.UserMessageRepository
import javax.inject.Inject

class AddUserMessage @Inject constructor(
    private val userMessageRepository: UserMessageRepository,
) {
    suspend operator fun invoke(message : String) {
        userMessageRepository.addUserMessage(message)
    }
}