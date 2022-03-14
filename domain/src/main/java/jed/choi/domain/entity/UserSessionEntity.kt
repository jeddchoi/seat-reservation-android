package jed.choi.domain.entity

import jed.choi.domain.UserState
import java.text.DateFormat
import java.util.*


/**
 * 타임아웃 관련 상태는 겹치지 않음
 *
 */
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


data class UserSessionEntity(
    val userState: UserState = UserState.LOGGED_OUT,
    val loggedInAt: Long? = null,
    val reservedAt: Long? = null,
    val cancelReservationAfter: Long = 600L, // 10 mins in seconds 60*10
    val startedUsingAt: Long? = null,
    val startedBusinessAt: Long? = null,
    val endBusinessAfter: Long = 1800L, // 30 mins in seconds 60*30
    val awayAt: Long? = null,
    val endAwayAfter: Long = 60L, // 1 min in seconds
    val timedOutAt: Long? = null,
    val blockedAt: Long? = null,
    val endBlockAfter: Long = 60L // 1 min in seconds
) {
    /**
     * [UserState.LOGGED_OUT] -> [UserState.LOGGED_IN]
     */
    fun onLogin() = copy(
        userState = UserState.LOGGED_IN,
        loggedInAt = System.currentTimeMillis()
    )

    fun onLogout() = UserSessionEntity()


    /**
     * [UserState.LOGGED_IN] -> [UserState.RESERVED]
     */
    fun onReserve() = copy(
        userState = UserState.RESERVED,
        reservedAt = System.currentTimeMillis()
    )

    /**
     * [UserState.RESERVED] -> [UserState.USING]
     */
    fun onStartUsing() = copy(
        userState = UserState.USING,
        reservedAt = null,
        startedUsingAt = System.currentTimeMillis()
    )

    /**
     * [UserState.RESERVED] -> [UserState.LOGGED_IN]
     * [UserState.USING] -> [UserState.LOGGED_IN]
     * [UserState.BUSINESS] -> [UserState.LOGGED_IN]
     * [UserState.AWAY] -> [UserState.LOGGED_IN]
     */
    fun onStopUsing() = copy(
        userState = UserState.LOGGED_IN,
        reservedAt = null,
        startedUsingAt = null,
        startedBusinessAt = null,
        awayAt = null
    )

    /**
     * [UserState.USING] -> [UserState.BUSINESS]
     * [UserState.AWAY] -> [UserState.BUSINESS]
     */
    fun onStartBusiness() = copy(
        userState = UserState.BUSINESS,
        startedBusinessAt = System.currentTimeMillis(),
        awayAt = null
    )

    /**
     * [UserState.USING] -> [UserState.AWAY]
     */
    fun onLeaveAway() = copy(
        userState = UserState.AWAY,
        awayAt = System.currentTimeMillis()
    )

    /**
     * [UserState.BUSINESS] -> [UserState.USING]
     */
    fun onStopBusiness() = copy(
        userState = UserState.USING,
        startedBusinessAt = null
    )

    /**
     * [UserState.AWAY] -> [UserState.USING]
     */
    fun onArrive() = copy(
        userState = UserState.USING,
        awayAt = null,
    )

    /**
     * [UserState.NEED_USER_CHECK] -> [UserState.LOGGED_IN]
     */
    fun onUserChecked() = copy(
        userState = UserState.LOGGED_IN,
        reservedAt = null,
        startedUsingAt = null,
        startedBusinessAt = null,
        awayAt = null,
        timedOutAt = null,
        blockedAt = null,
    )


    fun isValidState(): Boolean =
        when (userState) {
            UserState.LOGGED_OUT -> {
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
                        (!isTimeout)
            }
        }


    /**
     * starting epoch time of state
     * it this is null, user logged out or didn't start not yet
     */
    fun getStartingTime(state: UserState) = when (state) {
        UserState.LOGGED_OUT -> null
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
    fun getTimeoutValue(state: UserState) = when (state) {
        UserState.LOGGED_OUT,
        UserState.LOGGED_IN,
        UserState.USING,
        UserState.NEED_USER_CHECK -> null
        UserState.RESERVED -> cancelReservationAfter
        UserState.BUSINESS -> endBusinessAfter
        UserState.AWAY -> endAwayAfter
        UserState.BLOCKED -> endBlockAfter
    }

    /**
     * elapsed time in milliseconds after [state] started
     */
    fun getElapsedTime(state: UserState) =
        System.currentTimeMillis().minus(getStartingTime(state) ?: 0L)


    /**
     * specific epoch time of timeout in this state
     * it is same as [getStartingTime] + [getTimeoutValue]
     * null value means that state don't timeout
     */
    val willTimeoutAt: Long?
        get() = getTimeoutValue(userState)?.plus(getStartingTime(userState) ?: 0L)


    val isTimeout
        get() = willTimeoutAt?.let { it > System.currentTimeMillis() } ?: false

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
            ((elapsedTime / total) * MAX_PROGRESS).toInt()
        } ?: MAX_PROGRESS

    val remainingProgressPermillage
        get() = MAX_PROGRESS - elapsedProgressPermillage


    companion object {
        const val MAX_PROGRESS = 1000

        const val DEFAULT_CANCEL_RESERVATION_AFTER = 600L
        const val DEFAULT_END_BUSINESS_AFTER = 1800L
        const val DEFAULT_END_AWAY_AFTER = 60L
        const val DEFAULT_END_BLOCK_AFTER = 60L
    }


    //val userState: UserState = UserState.LOGGED_OUT,
    //    val loggedInAt: Long? = null,
    //    val reservedAt: Long? = null,
    //    val cancelReservationAfter: Long = 600L, // 10 mins in seconds 60*10
    //    val startedUsingAt: Long? = null,
    //    val startedBusinessAt: Long? = null,
    //    val endBusinessAfter: Long = 1800L, // 30 mins in seconds 60*30
    //    val awayAt: Long? = null,
    //    val endAwayAfter: Long = 60L, // 1 min in seconds
    //    val timedOutAt: Long? = null,
    //    val blockedAt: Long? = null,
    //    val endBlockAfter: Long = 60L // 1 min in seconds
    override fun toString(): String {
        return """
    userState           : ${userState.name}
    loggedInAt          : ${loggedInAt?.toFormattedDateString() ?: ""}
    reservedAt          : ${reservedAt?.toFormattedDateString() ?: ""} < $cancelReservationAfter
    startedUsingAt      : ${startedUsingAt?.toFormattedDateString() ?: ""}
    startedBusinessAt   : ${startedBusinessAt?.toFormattedDateString() ?: ""} < $endBusinessAfter
    awayAt              : ${awayAt?.toFormattedDateString() ?: ""} < $endAwayAfter
    timedOutAt          : ${timedOutAt?.toFormattedDateString() ?: ""}
    blockedAt           : ${blockedAt?.toFormattedDateString() ?: ""} < $endBlockAfter
        """.trimIndent()
    }



}

fun Long.toFormattedDateString() = DateFormat.getDateInstance().format(this).toString()









