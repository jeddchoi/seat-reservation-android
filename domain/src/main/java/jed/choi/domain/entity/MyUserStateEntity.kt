package jed.choi.domain.entity

import jed.choi.domain.UserState

data class MyUserStateEntity(
    val user: UserEntity? = null,
    val mySeat: SeatEntity? = null, // if before user reserve, this is null
    val mySession: UserSessionEntity? = null, // if before user login, this is null
) {

    fun isValid() = mySession?.isValidState() == true && when (mySession.userState) {
        null,
        UserState.LOGGED_IN,
        UserState.NEED_USER_CHECK,
        UserState.BLOCKED -> mySeat == null
        UserState.RESERVED,
        UserState.USING,
        UserState.BUSINESS,
        UserState.AWAY -> mySeat != null
    }
}

