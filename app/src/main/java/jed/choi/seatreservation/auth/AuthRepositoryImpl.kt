package jed.choi.seatreservation.auth

import com.google.firebase.auth.FirebaseAuth
import jed.choi.domain.entity.UserEntity
import jed.choi.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {
    private val firebaseUser = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(it.currentUser).isSuccess
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }


    override suspend fun login(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun register(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getLoggedInUser(): Flow<UserEntity> = firebaseUser.map {
        UserEntity(it?.displayName, it?.email, it?.photoUrl?.toString(), it != null)
    }


}