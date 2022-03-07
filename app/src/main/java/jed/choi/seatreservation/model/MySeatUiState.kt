package jed.choi.seatreservation.model

import jed.choi.domain.Seat
import jed.choi.domain.UserSession
import jed.choi.domain.UserState
import jed.choi.ui_core.UiState

data class MySeatUiState(
    val myName: String = "Unknown",
    val mySeat: Seat? = null, // if before user reserve, this is null
    val mySession: UserSession = UserSession(),
    val showRemainingTime: Boolean = true,
) : UiState

val MySeatUiState.showMyStatePanel: Boolean
    get() = mySession.userState != UserState.LOGGED_OUT && mySeat != null
