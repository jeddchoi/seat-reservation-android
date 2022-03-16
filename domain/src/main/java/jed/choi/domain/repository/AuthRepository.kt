package jed.choi.domain.repository

import jed.choi.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {


    fun observeAuthUid(): Flow<String?>
    fun observeUser(uid: String): Flow<UserEntity?>


    //    suspend fun createUserInFirestoreFlow(): Flow<Response<Boolean>>
//    suspend fun signInWithGoogleFlow(idToken: String): Flow<Response<Boolean>>
//    suspend fun signOutFlow(): Flow<Response<Boolean>>

    fun isUserAuthenticatedInFirebase(): Boolean
    suspend fun createUserInFirestore(): Boolean

    // returns isNewUser
    suspend fun signInWithGoogle(idToken: String): Boolean
    suspend fun signOut()
}