package jed.choi.seatreservation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException


/**
 * Firebase Auth
 */
fun FirebaseAuth.observeUser() = callbackFlow {
    val authStateListener = FirebaseAuth.AuthStateListener {
        trySend(it.currentUser).isSuccess
    }
    addAuthStateListener(authStateListener)
    awaitClose {
        removeAuthStateListener(authStateListener)
    }
}

/**
 * Firebase Realtime Database
 */

suspend fun DatabaseReference.awaitsSingle(): DataSnapshot? =
    suspendCancellableCoroutine { continuation ->
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    continuation.resume(snapshot) {
                        continuation.resumeWithException(it)
                    }
                } catch (exception: Exception) {
                    continuation.resumeWithException(exception)
                }
            }
        }
        continuation.invokeOnCancellation { this.removeEventListener(listener) }
        this.addListenerForSingleValueEvent(listener)
    }


fun DatabaseReference.observeValue() =
    callbackFlow {
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot).isSuccess
            }
        }
        addValueEventListener(listener)
        awaitClose { removeEventListener(listener) }
    }


fun DatabaseReference.observeChildEvent() = callbackFlow {
    val listener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {
            close(error.toException())
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            trySend(snapshot).isSuccess
        }

        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            trySend(snapshot).isSuccess
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            trySend(snapshot).isSuccess
        }
    }

    addChildEventListener(listener)
    awaitClose { removeEventListener(listener) }
}

/**
 * Firebase Firestore
 */
fun DocumentReference.observeValue() = callbackFlow {
    // Registers callback to firestore, which will be called on new events
    val subscription = addSnapshotListener { snapshot, error ->
        if (snapshot == null) {
            return@addSnapshotListener
        }
        if (error != null) {
            close(error)
        }

        // Sends events to the flow! Consumers will get the new events
        try {
            trySend(snapshot).isSuccess
        } catch (e: Throwable) {
            close(e)
        }
    }

    // The callback inside awaitClose will be executed when the flow is
    // either closed or cancelled.
    // In this case, remove the callback from Firestore
    awaitClose { subscription.remove() }
}