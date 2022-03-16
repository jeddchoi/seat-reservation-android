package jed.choi.seatreservation.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import jed.choi.domain.UserState
import jed.choi.domain.entity.UserSessionEntity
import jed.choi.domain.repository.SessionRepository
import jed.choi.seatreservation.awaitsSingle
import jed.choi.seatreservation.di.USER_SESSIONS_REF
import jed.choi.seatreservation.observeValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @Named(USER_SESSIONS_REF) private val userSessionsRef: DatabaseReference,
) : SessionRepository {

    override fun observeUserSession(uid: String) =
        userSessionsRef.child(uid).observeValue().map { it.getValue(UserSessionEntity::class.java) }
            .flowOn(Dispatchers.IO)

    override suspend fun getUserSession() = auth.currentUser?.uid?.let {
        userSessionsRef.child(it).awaitsSingle()?.getValue(UserSessionEntity::class.java)
    }


//    override suspend fun onSignInUserFlow() = flow {
//        try {
//            emit(Response.Loading)
//
//            if (onSignInUser()) {
//                emit(Response.Success(true))
//            }
//        } catch (e: Exception) {
//            emit(Response.Failure(e.message ?: UNEXPECTED_ERROR_MESSAGE))
//        }
//    }.flowOn(Dispatchers.IO)

    //    override suspend fun onSignOutUserFlow() = flow {
//        try {
//            emit(Response.Loading)
//
//            if (onSignOutUser()) {
//                emit(Response.Success(true))
//            }
//        } catch (e: Exception) {
//            emit(Response.Failure(e.message ?: UNEXPECTED_ERROR_MESSAGE))
//        }
//    }.flowOn(Dispatchers.IO)


    private suspend fun updateUserSession(childUpdates: HashMap<String, Any?>) =
        withContext(Dispatchers.IO) {
            auth.currentUser?.uid?.let {
                userSessionsRef.child(it).updateChildren(childUpdates).await().also {
                    return@withContext true
                }
            }
            return@withContext false
        }

    override suspend fun onSignInUser() = withContext(Dispatchers.IO) {
        auth.currentUser?.uid?.let {
            val existingSession =
                userSessionsRef.child(it).awaitsSingle()?.getValue(UserSessionEntity::class.java)
            val childUpdates = hashMapOf<String, Any?>(
                UserSessionEntity.LOGGED_IN_AT to ServerValue.TIMESTAMP,
            )
            if (existingSession == null) {
                childUpdates[UserSessionEntity.USER_STATE] = UserState.LOGGED_IN
            }

            userSessionsRef.child(it).updateChildren(childUpdates).await().also {
                return@withContext true
            }
        }
        return@withContext false
    }

    override suspend fun onSignOutUser() = updateUserSession(
        hashMapOf(
            UserSessionEntity.LOGGED_IN_AT to null,
        )
    )

    override suspend fun onReserveSeat(timeoutInSeconds: Long) = updateUserSession(
        hashMapOf(
            UserSessionEntity.USER_STATE to UserState.RESERVED,
            UserSessionEntity.RESERVED_AT to ServerValue.TIMESTAMP,
            UserSessionEntity.CANCEL_RESERVATION_AFTER to timeoutInSeconds,
        )
    )

    override suspend fun onStartUsing() = updateUserSession(
        hashMapOf(
            UserSessionEntity.USER_STATE to UserState.USING,
            UserSessionEntity.RESERVED_AT to null,
            UserSessionEntity.CANCEL_RESERVATION_AFTER to null,
            UserSessionEntity.STARTED_USING_AT to ServerValue.TIMESTAMP,
        )
    )

    override suspend fun onStopUsing() = updateUserSession(
        hashMapOf(
            UserSessionEntity.USER_STATE to UserState.LOGGED_IN,
            UserSessionEntity.RESERVED_AT to null,
            UserSessionEntity.CANCEL_RESERVATION_AFTER to null,
            UserSessionEntity.STARTED_USING_AT to null,
            UserSessionEntity.STARTED_BUSINESS_AT to null,
            UserSessionEntity.END_BUSINESS_AFTER to null,
            UserSessionEntity.AWAY_AT to null,
            UserSessionEntity.END_AWAY_AFTER to null,
        )
    )


    override suspend fun onStartBusiness(timeoutInSeconds: Long) = updateUserSession(
        hashMapOf(
            UserSessionEntity.USER_STATE to UserState.BUSINESS,
            UserSessionEntity.STARTED_BUSINESS_AT to ServerValue.TIMESTAMP,
            UserSessionEntity.END_BUSINESS_AFTER to timeoutInSeconds,
            UserSessionEntity.AWAY_AT to null,
            UserSessionEntity.END_AWAY_AFTER to null,
        )
    )

    override suspend fun onStopBusiness() = updateUserSession(
        hashMapOf(
            UserSessionEntity.USER_STATE to UserState.USING,
            UserSessionEntity.STARTED_BUSINESS_AT to null,
            UserSessionEntity.END_BUSINESS_AFTER to null,
        )
    )

    override suspend fun onLeaveAwaySeat(timeoutInSeconds: Long) = updateUserSession(
        hashMapOf(
            UserSessionEntity.USER_STATE to UserState.AWAY,
            UserSessionEntity.AWAY_AT to ServerValue.TIMESTAMP,
            UserSessionEntity.END_AWAY_AFTER to timeoutInSeconds,
        )
    )


    override suspend fun onResumeUsingSeat() = updateUserSession(
        hashMapOf(
            UserSessionEntity.USER_STATE to UserState.USING,
            UserSessionEntity.AWAY_AT to null,
            UserSessionEntity.END_AWAY_AFTER to null,
        )
    )

    override suspend fun onUserCheckTimeout() = updateUserSession(
        hashMapOf(
            UserSessionEntity.USER_STATE to UserState.LOGGED_IN,
            UserSessionEntity.RESERVED_AT to null,
            UserSessionEntity.CANCEL_RESERVATION_AFTER to null,
            UserSessionEntity.STARTED_USING_AT to null,
            UserSessionEntity.STARTED_BUSINESS_AT to null,
            UserSessionEntity.END_BUSINESS_AFTER to null,
            UserSessionEntity.AWAY_AT to null,
            UserSessionEntity.END_AWAY_AFTER to null,
            UserSessionEntity.TIMED_OUT_AT to null,
            UserSessionEntity.BLOCKED_AT to null,
            UserSessionEntity.END_BLOCK_AFTER to null,
        )
    )

    companion object {
        const val TAG = "SessionRepositoryImpl"
    }
}