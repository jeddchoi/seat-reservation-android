package jed.choi.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jed.choi.local.model.UserMessageLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface UserMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(userMessage: UserMessageLocal)

    @Query("SELECT * FROM user_messages ORDER BY id ASC LIMIT 1 ")
    fun getOldest(): Flow<UserMessageLocal?>

    @Query("SELECT * FROM user_messages")
    suspend fun getAll(): List<UserMessageLocal>

    @Query("DELETE FROM user_messages")
    suspend fun deleteAll()

    @Query("DELETE FROM user_messages WHERE :id=id")
    suspend fun remove(id: Long)
}
