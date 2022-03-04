package jed.choi.seatreservation.model

import jed.choi.domain.Seat
import jed.choi.domain.UserSession
import jed.choi.domain.UserState
import jed.choi.ui_core.UiState

data class MySeatUiState(
    val myName: String = "Unknown",
    val myState: UserState = UserState.LOGGED_OUT,
    val mySeat: Seat? = null, // if before user reserve, this is null
    val mySession: UserSession? = null, // if before user login, this is null
) : UiState

val MySeatUiState.showMyStatePanel : Boolean
    get() = myState != UserState.LOGGED_OUT && mySeat != null && mySession != null
