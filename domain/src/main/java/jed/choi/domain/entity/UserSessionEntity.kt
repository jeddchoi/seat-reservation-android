package jed.choi.domain.entity

import jed.choi.domain.UserState
import java.text.DateFormat


/**
 * 타임아웃 관련 상태는 겹치지 않음
 *
 */


data class UserSessionEntity(
    val userState: UserState? = null,
    val loggedInAt: Long? = null,
    val reservedAt: Long? = null,
    val cancelReservationAfter: Long? = null, // 10 mins in seconds 60*10
    val startedUsingAt: Long? = null,
    val startedBusinessAt: Long? = null,
    val endBusinessAfter: Long? = null, // 30 mins in seconds 60*30
    val awayAt: Long? = null,
    val endAwayAfter: Long? = null, // 1 min in seconds
    val timedOutAt: Long? = null,
    val blockedAt: Long? = null,
    val endBlockAfter: Long? = null // 1 min in seconds
) {
    //    /**
//     *  [UserState.LOGGED_IN]
//     */
//    fun onSignIn() = copy(
//        userState = UserState.LOGGED_IN,
//        loggedInAt = System.currentTimeMillis()
//    )
//
//    fun onSignOut() = UserSessionEntity()
//
//
//    /**
//     * [UserState.LOGGED_IN] -> [UserState.RESERVED]
//     */
//    fun onReserve() = copy(
//        userState = UserState.RESERVED,
//        reservedAt = System.currentTimeMillis()
//    )
//
//    /**
//     * [UserState.RESERVED] -> [UserState.USING]
//     */
//    fun onStartUsing() = copy(
//        userState = UserState.USING,
//        reservedAt = null,
//        startedUsingAt = System.currentTimeMillis()
//    )
//
//    /**
//     * [UserState.RESERVED] -> [UserState.LOGGED_IN]
//     * [UserState.USING] -> [UserState.LOGGED_IN]
//     * [UserState.BUSINESS] -> [UserState.LOGGED_IN]
//     * [UserState.AWAY] -> [UserState.LOGGED_IN]
//     */
//    fun onStopUsing() = copy(
//        userState = UserState.LOGGED_IN,
//        reservedAt = null,
//        startedUsingAt = null,
//        startedBusinessAt = null,
//        awayAt = null
//    )
//
//    /**
//     * [UserState.USING] -> [UserState.BUSINESS]
//     * [UserState.AWAY] -> [UserState.BUSINESS]
//     */
//    fun onStartBusiness() = copy(
//        userState = UserState.BUSINESS,
//        startedBusinessAt = System.currentTimeMillis(),
//        awayAt = null
//    )
//
//    /**
//     * [UserState.BUSINESS] -> [UserState.USING]
//     */
//    fun onStopBusiness() = copy(
//        userState = UserState.USING,
//        startedBusinessAt = null
//    )
//
//    /**
//     * [UserState.USING] -> [UserState.AWAY]
//     */
//    fun onLeaveAway() = copy(
//        userState = UserState.AWAY,
//        awayAt = System.currentTimeMillis()
//    )
//
//
//    /**
//     * [UserState.AWAY] -> [UserState.USING]
//     */
//    fun onResumeUsing() = copy(
//        userState = UserState.USING,
//        awayAt = null,
//    )
//
//    /**
//     * [UserState.NEED_USER_CHECK] -> [UserState.LOGGED_IN]
//     */
//    fun onUserChecked() = copy(
//        userState = UserState.LOGGED_IN,
//        reservedAt = null,
//        startedUsingAt = null,
//        startedBusinessAt = null,
//        awayAt = null,
//        timedOutAt = null,
//        blockedAt = null,
//    )
//    fun checkUserState() = userState ?: if (timedOutAt != null) UserState.NEED_USER_CHECK
//    else if (blockedAt != null && endBlockAfter != null) UserState.BLOCKED
//    else if (awayAt != null && endAwayAfter != null) UserState.AWAY
//    else if (startedBusinessAt != null && endBusinessAfter != null) UserState.BUSINESS
//    else if (startedUsingAt != null) UserState.USING
//    else if (reservedAt != null && cancelReservationAfter != null) UserState.RESERVED
//    else TODO()


    fun isValidState(): Boolean =
        when (userState) {
            null -> {
                (loggedInAt == null) &&
                        (reservedAt == null) &&
                        (startedUsingAt == null) &&
                        (startedBusinessAt == null) &&
                        (awayAt == null) &&
                        (timedOutAt == null) &&
                        (blockedAt == null)
            }
            UserState.LOGGED_IN -> {
                (loggedInAt != null) &&
                        (reservedAt == null) &&
                        (startedUsingAt == null) &&
                        (startedBusinessAt == null) &&
                        (awayAt == null) &&
                        (timedOutAt == null) &&
                        (blockedAt == null)
            }
            UserState.RESERVED -> {
                (loggedInAt != null) &&
                        (reservedAt != null) &&
                        (cancelReservationAfter != null) &&
                        (startedUsingAt == null) &&
                        (startedBusinessAt == null) &&
                        (awayAt == null) &&
                        (timedOutAt == null) &&
                        (blockedAt == null) &&
                        (!isTimeout)
            }
            UserState.USING -> {
                (loggedInAt != null) &&
                        (reservedAt == null) &&
                        (startedUsingAt != null) &&
                        (startedBusinessAt == null) &&
                        (awayAt == null) &&
                        (timedOutAt == null) &&
                        (blockedAt == null)
            }
            UserState.BUSINESS -> {
                (loggedInAt != null) &&
                        (reservedAt == null) &&
                        (startedUsingAt != null) &&
                        (startedBusinessAt != null) &&
                        (endBusinessAfter != null) &&
                        (awayAt == null) &&
                        (timedOutAt == null) &&
                        (blockedAt == null) &&
                        (!isTimeout)
            }
            UserState.AWAY -> {
                (loggedInAt != null) &&
                        (reservedAt == null) &&
                        (startedUsingAt != null) &&
//                (startedBusinessAt == null) &&
                        (awayAt != null) &&
                        (endAwayAfter != null) &&
                        (timedOutAt == null) &&
                        (blockedAt == null) &&
                        (!isTimeout)
            }
            UserState.NEED_USER_CHECK -> {
                (loggedInAt != null) &&
//                (reservedAt == null) &&
//                (startedUsingAt == null) &&
                        (startedBusinessAt == null) &&
//                (awayAt == null) &&
                        (timedOutAt != null)
//                (blockedAt == null)
            }
            UserState.BLOCKED -> {
                (loggedInAt != null) &&
//                (reservedAt == null) &&
//                (startedUsingAt == null) &&
//                (startedBusinessAt == null) &&
//                (awayAt == null) &&
                        (timedOutAt == null) &&
                        (blockedAt != null) &&
                        (endBlockAfter != null) &&
                        (!isTimeout)
            }
        }


    /**
     * starting epoch time of state
     * it this is null, user logged out or didn't start not yet
     */
    fun getStartingTime(state: UserState?) = when (state) {
        null -> null
        UserState.LOGGED_IN -> loggedInAt
        UserState.RESERVED -> reservedAt
        UserState.USING -> startedUsingAt
        UserState.BUSINESS -> startedBusinessAt
        UserState.AWAY -> awayAt
        UserState.NEED_USER_CHECK -> timedOutAt
        UserState.BLOCKED -> blockedAt
    }

    /**
     * total time in milliseconds of timeout
     * null value means that state don't timeout
     */
    fun getTimeoutValue(state: UserState?) = when (state) {
        null,
        UserState.LOGGED_IN,
        UserState.USING,
        UserState.NEED_USER_CHECK -> null
        UserState.RESERVED -> cancelReservationAfter
        UserState.BUSINESS -> endBusinessAfter
        UserState.AWAY -> endAwayAfter
        UserState.BLOCKED -> endBlockAfter
    }?.times(1000L)

    /**
     * elapsed time in milliseconds after [state] started
     */
    fun getElapsedTime(state: UserState?) =
        System.currentTimeMillis().minus(getStartingTime(state) ?: 0L)


    /**
     * specific epoch time of timeout in this state
     * it is same as [getStartingTime] + [getTimeoutValue]
     * null value means that state don't timeout
     */
    val willTimeoutAt: Long?
        get() = getTimeoutValue(userState)?.plus(getStartingTime(userState) ?: 0L)


    val isTimeout
        get() = willTimeoutAt?.let { it < System.currentTimeMillis() } ?: false

    /**
     * remaining time in milliseconds before timeout in this state
     * null value means that state don't timeout
     */
    val remainingTimeBeforeTimeout
        get() = willTimeoutAt?.minus(System.currentTimeMillis())


    /**
     * elapsed time in milliseconds in this state
     */
    val elapsedTime
        get() = getElapsedTime(userState)


    val elapsedProgressPermillage
        get() = getTimeoutValue(userState)?.let { total ->
            ((elapsedTime.toDouble() / total) * MAX_PROGRESS).toInt()
        } ?: MAX_PROGRESS

    val remainingProgressPermillage
        get() = MAX_PROGRESS - elapsedProgressPermillage


    fun toMap(): Map<String, Any?> = mapOf(
        USER_STATE to userState,
        LOGGED_IN_AT to loggedInAt,
        RESERVED_AT to reservedAt,
        CANCEL_RESERVATION_AFTER to cancelReservationAfter,
        STARTED_USING_AT to startedUsingAt,
        STARTED_BUSINESS_AT to startedBusinessAt,
        END_BUSINESS_AFTER to endBusinessAfter,
        AWAY_AT to awayAt,
        END_AWAY_AFTER to endAwayAfter,
        TIMED_OUT_AT to timedOutAt,
        BLOCKED_AT to blockedAt,
        END_BLOCK_AFTER to endBlockAfter,
    )


    override fun toString(): String {
        return """
    userState                   : ${userState?.name}
    loggedInAt                  : ${loggedInAt?.toFormattedDateString() ?: ""}
    reservedAt                  : ${reservedAt?.toFormattedDateString() ?: ""} < $cancelReservationAfter
    startedUsingAt              : ${startedUsingAt?.toFormattedDateString() ?: ""}
    startedBusinessAt           : ${startedBusinessAt?.toFormattedDateString() ?: ""} < $endBusinessAfter
    awayAt                      : ${awayAt?.toFormattedDateString() ?: ""} < $endAwayAfter
    timedOutAt                  : ${timedOutAt?.toFormattedDateString() ?: ""}
    blockedAt                   : ${blockedAt?.toFormattedDateString() ?: ""} < $endBlockAfter
    willTimeoutAt               : ${willTimeoutAt?.toFormattedDateString() ?: ""}
        """.trimIndent()
    }

    companion object {
        const val MAX_PROGRESS = 1000

        const val DEFAULT_CANCEL_RESERVATION_AFTER = 600L
        const val DEFAULT_END_BUSINESS_AFTER = 1800L
        const val DEFAULT_END_AWAY_AFTER = 60L
        const val DEFAULT_END_BLOCK_AFTER = 60L


        const val USER_STATE = "userState"
        const val LOGGED_IN_AT = "loggedInAt"
        const val RESERVED_AT = "reservedAt"
        const val CANCEL_RESERVATION_AFTER = "cancelReservationAfter"
        const val STARTED_USING_AT = "startedUsingAt"
        const val STARTED_BUSINESS_AT = "startedBusinessAt"
        const val END_BUSINESS_AFTER = "endBusinessAfter"
        const val AWAY_AT = "awayAt"
        const val END_AWAY_AFTER = "endAwayAfter"
        const val TIMED_OUT_AT = "timedOutAt"
        const val BLOCKED_AT = "blockedAt"
        const val END_BLOCK_AFTER = "endBlockAfter"

    }
}

fun Long.toFormattedDateString() = DateFormat.getDateTimeInstance().format(this).toString()









