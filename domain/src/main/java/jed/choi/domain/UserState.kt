package jed.choi.domain

enum class UserState {
    LOGGED_OUT,
    LOGGED_IN,
    RESERVED,
    USING,
    BUSINESS,
    AWAY,
    NEED_USER_CHECK, // after user check, it would be LOGGED_IN because this state is temporary
    BLOCKED,
}