package jed.choi.domain.repository

import jed.choi.domain.entity.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isUserAuthenticatedInFirebase(): Boolean

    suspend fun signInWithGoogle(idToken: String): Flow<Response<Boolean>>

    suspend fun signOut(): Flow<Response<Boolean>>

    fun getFirebaseAuthState(): Flow<Boolean>


    suspend fun createUserInRealtimeDatabase(): Flow<Response<Boolean>>
}