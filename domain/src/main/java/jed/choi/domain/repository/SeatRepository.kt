package jed.choi.domain.repository

import jed.choi.domain.entity.SeatEntity
import kotlinx.coroutines.flow.Flow

interface SeatRepository {
    fun observeUserSeatPath(uid: String): Flow<String?>
    fun observeUserSeat(uid: String): Flow<SeatEntity?>


    suspend fun onReserveSeat(path: String): Boolean
    suspend fun onStartUsing(): Boolean
    suspend fun onStopUsing(): Boolean
    suspend fun onLeaveAwaySeat(): Boolean
    suspend fun onResumeUsingSeat(): Boolean
    suspend fun onStartBusiness(): Boolean
    suspend fun onStopBusiness(): Boolean


}