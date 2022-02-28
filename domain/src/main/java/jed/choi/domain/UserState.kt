package jed.choi.domain

enum class UserState {
    LOGGED_OUT,
    LOGGED_IN,
    RESERVED,
    USING,
    AWAY,
    AWAY_INTENTIONAL,
    TIMED_OUT, // after user check, it would be LOGGED_IN
    BLOCKED,
}