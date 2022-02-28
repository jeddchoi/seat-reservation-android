package jed.choi.ui_core

abstract class UiState {
    val userMessages: List<UserMessage> = emptyList()
}

data class UserMessage(val id: Long, val message: String)
