package jed.choi.domain

data class UserSession(
    val loggedInAt: Long? = null,
    val reservedAt: Long? = null,
    val startedUsingAt: Long? = null,
    val awayAt: Long? = null,
    val awayIntentionalAt: Long? = null,
    val timedOutAt: Long? = null,
    val blockedAt: Long? = null
)