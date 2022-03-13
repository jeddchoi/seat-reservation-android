package jed.choi.seatreservation.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jed.choi.seatreservation.BuildConfig


const val MY_IP_ADDRESS = "192.168.0.2"
const val PORT_NUM_AUTH = 9099
const val PORT_NUM_REALTIME_DATABASE = 9000
const val PORT_NUM_FUNCTIONS = 5001
const val PORT_NUM_PUB_SUB = 8085


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseAuth(
        // Potential dependencies of this type
    ): FirebaseAuth = FirebaseAuth.getInstance().also {
        if(BuildConfig.DEBUG) it.useEmulator(MY_IP_ADDRESS, PORT_NUM_AUTH)
    }

    @Provides
    fun provideFirebaseDatabase(
        // Potential dependencies of this type
    ): FirebaseDatabase = Firebase.database.also {
        if(BuildConfig.DEBUG) it.useEmulator(MY_IP_ADDRESS, PORT_NUM_REALTIME_DATABASE)
    }
}