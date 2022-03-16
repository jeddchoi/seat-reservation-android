package jed.choi.domain.repository

import jed.choi.domain.entity.UserSessionEntity
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun observeUserSession(uid: String): Flow<UserSessionEntity?>
    suspend fun getUserSession() : UserSessionEntity?

//    suspend fun onSignInUserFlow(): Flow<Response<Boolean>>
//    suspend fun onSignOutUserFlow(): Flow<Response<Boolean>>

    suspend fun onSignInUser(): Boolean
    suspend fun onSignOutUser(): Boolean

    suspend fun onReserveSeat(timeoutInSeconds: Long): Boolean
    suspend fun onStartUsing(): Boolean
    suspend fun onStopUsing(): Boolean
    suspend fun onStartBusiness(timeoutInSeconds: Long): Boolean
    suspend fun onStopBusiness(): Boolean
    suspend fun onLeaveAwaySeat(timeoutInSeconds: Long): Boolean
    suspend fun onResumeUsingSeat(): Boolean
    suspend fun onUserCheckTimeout() : Boolean
}