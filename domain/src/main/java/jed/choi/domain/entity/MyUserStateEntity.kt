package jed.choi.domain.entity

import jed.choi.domain.Seat
import jed.choi.domain.UserSession
import jed.choi.domain.UserState

data class MyUserStateEntity(
    val myName: String = "Unknown",
    val myState: UserState = UserState.LOGGED_OUT,
    val mySeat: Seat? = null, // if before user reserve, this is null
    val mySession: UserSession? = null, // if before user login, this is null
)
