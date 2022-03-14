package jed.choi.seatreservation.auth

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue.serverTimestamp
import jed.choi.domain.UNEXPECTED_ERROR_MESSAGE
import jed.choi.domain.entity.*
import jed.choi.domain.repository.AuthRepository
import jed.choi.seatreservation.di.USERS_REF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
    @Named(USERS_REF) private val usersRef: CollectionReference,
) : AuthRepository {

//    private fun userRefByUid(uid: String) = callbackFlow<Result<UserEntity?>> {
//        val eventListener = object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//                Log.i(TAG, "onCancelled: $error")
//                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
//            }
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val item = dataSnapshot.getValue(UserEntity::class.java)
//                Log.i(TAG, "onDataChange: $item")
//                this@callbackFlow.trySendBlocking(Result.success(item))
//            }
//        }
//
//        Log.i(TAG, "callbackflow: firebaseAuth.uid = $uid")
//        userReference.child(uid).addValueEventListener(eventListener)
//        awaitClose {
//            userReference.child(uid).removeEventListener(eventListener)
//        }
//    }


    override fun isUserAuthenticatedInFirebase(): Boolean = auth.currentUser != null

    override suspend fun signInWithGoogle(idToken: String): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            authResult.additionalUserInfo?.apply {
                emit(Response.Success(isNewUser))
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun signOut() = flow {
        try {
            emit(Response.Loading)
            googleSignInClient.signOut().await().also {
                emit(Response.Success(true))
            }
            auth.signOut()
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: UNEXPECTED_ERROR_MESSAGE))
        }
    }.flowOn(Dispatchers.IO)

    override fun getFirebaseAuthState() = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(it.currentUser != null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createUserInRealtimeDatabase() = flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.apply {
                usersRef.document(uid).set(mapOf(
                    NAME to displayName,
                    EMAIL_ADDRESS to email,
                    PROFILE_PHOTO_URL to photoUrl?.toString(),
                    CREATED_AT to serverTimestamp()
                )).await().also {
                    emit(Response.Success(true))
                }
            }
        } catch (e: Exception) {
            emit(Response.Failure(e.message ?: UNEXPECTED_ERROR_MESSAGE))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        const val USER_REFERENCE_NAME = "users"
        const val TAG = "AuthRepositoryImpl"
    }
}