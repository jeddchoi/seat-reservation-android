package jed.choi.seatreservation.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import jed.choi.domain.entity.SeatEntity
import jed.choi.domain.entity.SeatState
import jed.choi.domain.repository.SeatRepository
import jed.choi.seatreservation.awaitsSingle
import jed.choi.seatreservation.di.SEATS_REF
import jed.choi.seatreservation.di.USER_SEATS_REF
import jed.choi.seatreservation.observeValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SeatRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @Named(SEATS_REF) private val seatsRef: CollectionReference,
    @Named(USER_SEATS_REF) private val userSeatsRef: DatabaseReference,
) : SeatRepository {
    override fun observeUserSeatPath(uid: String) =
        userSeatsRef.child(uid).observeValue().map { it.getValue(String::class.java) }
            .flowOn(Dispatchers.IO)

    override fun observeUserSeat(uid: String) = observeUserSeatPath(uid).transformLatest { path ->
        if (path != null)
            emitAll(seatsRef.document("/$path").observeValue().map { it.toObject<SeatEntity>() })
        else emit(null)
    }.flowOn(Dispatchers.IO)


    override suspend fun onReserveSeat(path: String) = withContext(Dispatchers.IO) {
        auth.currentUser?.uid?.let { uid ->
            seatsRef.document(path).update(SeatEntity.STATE, SeatState.RESERVED).await()
            userSeatsRef.child(uid).setValue(path).await()
            return@withContext true
        }
        return@withContext false
    }

//    {"-MyB7-bs7z3bQDS8puKq":{"-MyB7tNK1siP7P5AEvzE":{"isCloseToWall":false,"isCloseToWindow":false,"isSoloFriendly":false,"name":"Seat_1","path":"-MyB6poExcCX0Hbtubv1/-MyB7-bs7z3bQDS8puKq/-MyB7tNK1siP7P5AEvzE","positionInSection":{"x":0,"y":0},"state":"RESERVED"}}}


    private suspend fun getUserSeatPath(uid: String) =
        userSeatsRef.child(uid).awaitsSingle()?.getValue(String::class.java)

    private suspend fun changeSeatState(seatState: SeatState) = withContext(Dispatchers.IO) {
        auth.currentUser?.uid?.let { uid ->
            getUserSeatPath(uid)?.let { path ->
                seatsRef.document("/$path").update(SeatEntity.STATE, seatState).await()
                return@withContext true
            }
        }
        return@withContext false
    }


    override suspend fun onStartUsing() = changeSeatState(SeatState.ACQUIRED)
    override suspend fun onStopUsing() = withContext(Dispatchers.IO) {
        auth.currentUser?.uid?.let { uid ->
            getUserSeatPath(uid)?.let { path ->
                seatsRef.document("/$path").update(SeatEntity.STATE, SeatState.IDLE).await()
                userSeatsRef.child(uid).setValue(null).await()
                return@withContext true
            }
        }
        return@withContext false
    }

    override suspend fun onLeaveAwaySeat() = changeSeatState(SeatState.AWAY)
    override suspend fun onResumeUsingSeat() = changeSeatState(SeatState.ACQUIRED)
    override suspend fun onStartBusiness() = changeSeatState(SeatState.ACQUIRED)
    override suspend fun onStopBusiness() = changeSeatState(SeatState.ACQUIRED)


    companion object {
        const val TAG = "SeatRepositoryImpl"
    }
}