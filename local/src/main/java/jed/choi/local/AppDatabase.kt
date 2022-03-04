package jed.choi.local

import androidx.room.Database
import androidx.room.RoomDatabase
import jed.choi.local.dao.UserMessageDao
import jed.choi.local.model.UserMessageLocal

@Database(entities = [UserMessageLocal::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userMessageDao(): UserMessageDao
}