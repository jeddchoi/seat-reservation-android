package jed.choi.domain.entity

import jed.choi.domain.Seat
import jed.choi.domain.UserSession
import jed.choi.domain.UserState

data class MyUserStateEntity(
    val myName: String? = null,
    val mySeat: Seat? = null, // if before user reserve, this is null
    val mySession: UserSession = UserSession(), // if before user login, this is null
) {

    fun isValid() = mySession.isValidState() && when (mySession.userState) {
        UserState.LOGGED_OUT,
        UserState.LOGGED_IN,
        UserState.NEED_USER_CHECK,
        UserState.BLOCKED -> mySeat == null
        UserState.RESERVED,
        UserState.USING,
        UserState.BUSINESS,
        UserState.AWAY -> mySeat != null
    }
}

