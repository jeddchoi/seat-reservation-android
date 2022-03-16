package jed.choi.seatreservation.repository

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.ktx.toObject
import jed.choi.domain.entity.UserEntity
import jed.choi.domain.entity.UserEntity.Companion.CREATED_AT
import jed.choi.domain.entity.UserEntity.Companion.EMAIL_ADDRESS
import jed.choi.domain.entity.UserEntity.Companion.NAME
import jed.choi.domain.entity.UserEntity.Companion.PROFILE_PHOTO_URL
import jed.choi.domain.repository.AuthRepository
import jed.choi.seatreservation.di.USERS_REF
import jed.choi.seatreservation.observeUser
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
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
    @Named(USERS_REF) private val usersRef: CollectionReference,
) : AuthRepository {


    override fun observeAuthUid() = auth.observeUser().map {
        it?.uid
    }.flowOn(Dispatchers.IO)

    override fun observeUser(uid: String) =
        usersRef.document(uid).observeValue().map {
            it.toObject<UserEntity>()
        }.flowOn(Dispatchers.IO)

//    override suspend fun createUserInFirestoreFlow(): Flow<Response<Boolean>> = flow {
//        try {
//            emit(Response.Loading)
//            if (createUserInFirestore()) {
//                emit(Response.Success(true))
//            }
//        } catch (e: Exception) {
//            emit(Response.Failure(e.message ?: UNEXPECTED_ERROR_MESSAGE))
//        }
//    }.flowOn(Dispatchers.IO)


    //    override suspend fun signInWithGoogleFlow(idToken: String): Flow<Response<Boolean>> = flow {
//        try {
//            emit(Response.Loading)
//            val credential = GoogleAuthProvider.getCredential(idToken, null)
//            val authResult = auth.signInWithCredential(credential).await()
//            authResult.additionalUserInfo?.apply {
//                emit(Response.Success(isNewUser))
//            }
//        } catch (e: Exception) {
//            emit(Response.Failure(e.message ?: e.toString()))
//        }
//    }.flowOn(Dispatchers.IO)
//
//    override suspend fun signOutFlow() = flow {
//        try {
//            emit(Response.Loading)
//            googleSignInClient.signOut().await()
//            auth.signOut()
//            emit(Response.Success(true))
//        } catch (e: Exception) {
//            emit(Response.Failure(e.message ?: UNEXPECTED_ERROR_MESSAGE))
//        }
//    }.flowOn(Dispatchers.IO)

    override fun isUserAuthenticatedInFirebase(): Boolean = auth.currentUser != null

    override suspend fun createUserInFirestore() = withContext(Dispatchers.IO) {
        auth.currentUser?.apply {
            usersRef.document(uid).set(
                mapOf(
                    NAME to displayName,
                    EMAIL_ADDRESS to email,
                    PROFILE_PHOTO_URL to photoUrl?.toString(),
                    CREATED_AT to serverTimestamp()
                )
            ).await().also {
                return@withContext true
            }
        }
        return@withContext false
    }

    override suspend fun signInWithGoogle(idToken: String) = withContext(Dispatchers.IO) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(credential).await()
        authResult.additionalUserInfo?.apply {
            return@withContext isNewUser
        }
        return@withContext false
    }

    override suspend fun signOut() = withContext(Dispatchers.IO) {
        googleSignInClient.signOut().await()
        auth.signOut()
    }

    companion object {
        const val USER_REFERENCE_NAME = "users"
        const val TAG = "AuthRepositoryImpl"
    }
}