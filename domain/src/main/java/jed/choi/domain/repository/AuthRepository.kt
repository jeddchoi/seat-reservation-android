package jed.choi.domain.repository

import jed.choi.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login() : Boolean
    suspend fun register() : Boolean

    fun getLoggedInUser() : Flow<UserEntity>
}