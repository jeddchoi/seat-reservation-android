package jed.choi.seatreservation.uistate

import jed.choi.domain.Seat
import jed.choi.domain.UserSession
import jed.choi.domain.UserState
import jed.choi.ui_core.UiState
import jed.choi.ui_core.UserMessage

data class MySeatUiState(
    val myName: String,
    val myState: UserState,
    val mySeat: Seat? = null, // if before user reserve, this is null
    val mySession: UserSession,
    override val userMessages: List<UserMessage> = emptyList(),
) : UiState
