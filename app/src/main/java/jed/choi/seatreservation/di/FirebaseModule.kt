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


const val MY_IP_ADDRESS = "192.168.0.2"
const val PORT_NUM_AUTH = 9099
const val PORT_NUM_FIRESTORE = 8080
const val PORT_NUM_REALTIME_DATABASE = 9000
const val PORT_NUM_FUNCTIONS = 5001
const val PORT_NUM_PUB_SUB = 8085


const val USERS_REF = "users"
const val SEATS_REF = "seats"
const val USER_SESSIONS_REF = "user_sessions"

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideFirebaseAuth(
        // Potential dependencies of this type
    ): FirebaseAuth = FirebaseAuth.getInstance().also {
        if(BuildConfig.DEBUG) it.useEmulator(MY_IP_ADDRESS, PORT_NUM_AUTH)
    }

    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance().also {
        if(BuildConfig.DEBUG) it.useEmulator(MY_IP_ADDRESS, PORT_NUM_FIRESTORE)
    }

    @Provides
    @Named(USERS_REF)
    fun provideUsersRef(db: FirebaseFirestore) = db.collection(USERS_REF)


    @Provides
    fun provideFirebaseDatabase(
        // Potential dependencies of this type
    ): FirebaseDatabase = Firebase.database.also {
        if(BuildConfig.DEBUG) it.useEmulator(MY_IP_ADDRESS, PORT_NUM_REALTIME_DATABASE)
    }

    @Provides
    @Named(SEATS_REF)
    fun provideRealtimeDatabaseSeatsRef(db: FirebaseDatabase) = db.reference.child(SEATS_REF)


    @Provides
    @Named(USER_SESSIONS_REF)
    fun provideRealtimeDatabaseUserSessionsRef(db: FirebaseDatabase) = db.reference.child(USER_SESSIONS_REF)
}