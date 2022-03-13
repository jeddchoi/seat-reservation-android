package jed.choi.seatreservation.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jed.choi.domain.entity.UserEntity
import jed.choi.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : AuthRepository {
    private val firebaseUser = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(it.currentUser).isSuccess
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    private val userReference = firebaseDatabase.getReference(USER_REFERENCE_NAME)
    private val userRef = firebaseUser.flatMapMerge {
        Log.i(TAG, "userRef transform: firebaseUser == $it")
        if (it != null) {
            callbackFlow<Result<UserEntity?>> {
                val eventListener = object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Log.i(TAG, "onCancelled: $error")
                        this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val item = dataSnapshot.getValue(UserEntity::class.java)
                        Log.i(TAG, "onDataChange: $item")
                        this@callbackFlow.trySendBlocking(Result.success(item))
                    }
                }

                Log.i(TAG, "callbackflow: firebaseAuth.uid = ${it.uid}")
                userReference.child(it.uid).addValueEventListener(eventListener)
                awaitClose {
                    userReference.child(it.uid).removeEventListener(eventListener)
                }
            }
        } else {
            emptyFlow()
        }
    }

    override suspend fun login(): Boolean {


        return true
    }


    override suspend fun register(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getLoggedInUser(): Flow<UserEntity> =
        combine(firebaseUser, userRef) { firebaseUser, userEntity ->
            Log.i(TAG, "getLoggedInUser: $firebaseUser, $userEntity")
            if (firebaseUser != null && userEntity.isSuccess && userEntity.getOrNull() != null) {
                UserEntity(
                    firebaseUser.uid,
                    firebaseUser.displayName,
                    firebaseUser.email,
                    firebaseUser.photoUrl.toString(),
                    true
                )
            } else {
                UserEntity()
            }
        }


    companion object {
        const val USER_REFERENCE_NAME = "users"
        const val TAG = "AuthRepositoryImpl"
    }
}