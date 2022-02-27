package jed.choi.domain


data class MySeatUiState(
    val myName: String,
    val myState: UserState,
    val mySeat: Seat? = null, // if before user reserve, this is null
    val mySession: UserSession,
)


data class UserSession(
    val loggedInAt: Long? = null,
    val reservedAt: Long? = null,
    val startedUsingAt: Long? = null,
    val awayAt: Long? = null,
    val awayIntentionalAt: Long? = null,
    val timedOutAt: Long? = null,
    val blockedAt: Long? = null
)

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
