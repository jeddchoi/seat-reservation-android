package jed.choi.seatreservation.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jed.choi.seatreservation.BuildConfig
import javax.inject.Named
import javax.inject.Singleton


const val MY_IP_ADDRESS = "192.168.0.2"
const val PORT_NUM_AUTH = 9099
const val PORT_NUM_FIRESTORE = 8080
const val PORT_NUM_REALTIME_DATABASE = 9000
const val PORT_NUM_FUNCTIONS = 5001
const val PORT_NUM_PUB_SUB = 8085


const val USERS_REF = "users"
const val USER_SEATS_REF = "user_seats"
const val USER_SESSIONS_REF = "user_sessions"
const val SEATS_REF = "seats"

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(
        // Potential dependencies of this type
    ): FirebaseAuth = FirebaseAuth.getInstance().also {
        if (BuildConfig.DEBUG) it.useEmulator(MY_IP_ADDRESS, PORT_NUM_AUTH)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance().also {
        if (BuildConfig.DEBUG) it.useEmulator(MY_IP_ADDRESS, PORT_NUM_FIRESTORE)
    }

    @Provides
    @Singleton
    @Named(USERS_REF)
    fun provideFirestoreUsersRef(db: FirebaseFirestore) = db.collection(USERS_REF)

    @Provides
    @Singleton
    @Named(SEATS_REF)
    fun provideFirestoreSeatsRef(db: FirebaseFirestore) = db.collection(SEATS_REF)


    @Provides
    @Singleton
    fun provideFirebaseDatabase(
        // Potential dependencies of this type
    ): FirebaseDatabase = Firebase.database.also {
        if (BuildConfig.DEBUG) it.useEmulator(MY_IP_ADDRESS, PORT_NUM_REALTIME_DATABASE)
    }

    @Provides
    @Singleton
    @Named(USER_SEATS_REF)
    fun provideRealtimeDatabaseUserSeatsRef(db: FirebaseDatabase) =
        db.reference.child(USER_SEATS_REF)


    @Provides
    @Singleton
    @Named(USER_SESSIONS_REF)
    fun provideRealtimeDatabaseUserSessionsRef(db: FirebaseDatabase) =
        db.reference.child(USER_SESSIONS_REF)

}