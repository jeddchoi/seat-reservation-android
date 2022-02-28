package jed.choi.ui_core

interface UiState {
    val userMessages: List<UserMessage>
}

data class UserMessage(val id: Long, val message: String)
