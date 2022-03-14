package jed.choi.seatreservation.model

import jed.choi.domain.Seat
import jed.choi.domain.UserState
import jed.choi.domain.entity.UserSessionEntity
import jed.choi.ui_core.UiState

data class MySeatUiState(
    val myName: String,
    val mySeat: Seat? = null, // if before user reserve, this is null
    val mySession: UserSessionEntity = UserSessionEntity(),
    val showRemainingTime: Boolean = true,
) : UiState {

    override fun toString(): String {
        return """
My Name    : $myName
My Seat    : $mySeat
My Session ----------- 
$mySession
        """.trimIndent()
    }
}

val MySeatUiState.showMyStatePanel: Boolean
    get() = mySession.userState != UserState.LOGGED_OUT && mySeat != null
