package jed.choi.seatreservation.model

import jed.choi.domain.UserState
import jed.choi.domain.entity.SeatEntity
import jed.choi.domain.entity.UserSessionEntity
import jed.choi.ui_core.UiState


enum class ProgressDisplayMode {
    REMAINING_TIME_PERMILLAGE,
    ELAPSED_TIME_PERMILLAGE,
    NONE,
}

data class MySeatUiState(
    val myName: String,
    val mySeat: SeatEntity? = null, // if before user reserve, this is null
    val mySession: UserSessionEntity? = null,
    val progressMaxTo0: Boolean = false
) : UiState {

    val progressDisplayMode: ProgressDisplayMode
        get() = when (mySession?.userState) {
            UserState.RESERVED,
            UserState.BUSINESS,
            UserState.AWAY,
            UserState.BLOCKED -> {
                if (!mySession.isTimeout) {
                    if (progressMaxTo0) ProgressDisplayMode.REMAINING_TIME_PERMILLAGE
                    else ProgressDisplayMode.ELAPSED_TIME_PERMILLAGE
                } else ProgressDisplayMode.NONE
            }
            else -> ProgressDisplayMode.NONE
        }

    val showMyStatePanel: Boolean
        get() = mySeat != null && when (mySession?.userState) {
            UserState.RESERVED,
            UserState.USING,
            UserState.BUSINESS,
            UserState.AWAY,
            UserState.NEED_USER_CHECK,
            UserState.BLOCKED -> true
            UserState.LOGGED_IN,
            null -> false
        }

    override fun toString(): String {
        return """
My Name    : $myName
My SeatEntity    : $mySeat
My Session ----------- 
$mySession
        """.trimIndent()
    }
}

