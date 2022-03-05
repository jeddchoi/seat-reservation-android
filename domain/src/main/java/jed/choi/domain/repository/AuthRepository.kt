package jed.choi.domain.repository

interface AuthRepository {
    suspend fun login() : Boolean
    suspend fun register() : Boolean
}