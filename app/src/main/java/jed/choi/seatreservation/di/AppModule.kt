package jed.choi.seatreservation.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseAuth(
        // Potential dependencies of this type
    ): FirebaseAuth = FirebaseAuth.getInstance()
}