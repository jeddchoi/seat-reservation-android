package jed.choi.seatreservation.repository

import android.util.Log
import com.google.firebase.database.*
import jed.choi.domain.UNEXPECTED_ERROR_MESSAGE
import jed.choi.domain.UserState
import jed.choi.domain.entity.*
import jed.choi.domain.repository.SessionRepository
import jed.choi.seatreservation.di.USER_SESSIONS_REF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor(
    @Named(USER_SESSIONS_REF) private val userSessionsRef: DatabaseReference,
) : SessionRepository {

    override fun getUserSession(uid: String): Flow<UserSessionEntity?> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(UserSessionEntity::class.java)
                Log.i(TAG, "onDataChange: $item")
                this@callbackFlow.trySendBlocking(item)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, "onCancelled: $error")
                this@callbackFlow.trySendBlocking(null)
            }
        }
        userSessionsRef.child(uid).addValueEventListener(valueEventListener)
        awaitClose {
            userSessionsRef.child(uid).removeEventListener(valueEventListener)
        }
    }

    override suspend fun createUserSessionInRealtimeDatabase(uid: String) = flow {
        try {
            emit(Response.Loading)

            userSessionsRef.child(uid).setValue(
                mapOf(
                    USER_STATE to UserState.LOGGED_IN,
                    LOGGED_IN_AT to ServerValue.TIMESTAMP,
                    RESERVED_AT to null,
                    CANCEL_RESERVATION_AFTER to UserSessionEntity.DEFAULT_CANCEL_RESERVATION_AFTER,
                    STARTED_USING_AT to null,
                    STARTED_BUSINESS_AT to null,
                    END_BUSINESS_AFTER to UserSessionEntity.DEFAULT_END_BUSINESS_AFTER,
                    AWAY_AT to null,
                    END_AWAY_AFTER to UserSessionEntity.DEFAULT_END_AWAY_AFTER,
                    TIMED_OUT_AT to null,
                    BLOCKED_AT to null,
                    END_BLOCK_AFTER to UserSessionEntity.DEFAULT_END_BLOCK_AFTER
                )
            ).await().also {
                emit(Response.Success(true))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: UNEXPECTED_ERROR_MESSAGE))
        }
    }.flowOn(Dispatchers.IO)


    companion object {
        const val TAG = "SessionRepositoryImpl"
    }
}