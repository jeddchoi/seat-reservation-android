package jed.choi.local.source

import jed.choi.data.model.UserMessageData
import jed.choi.data.repository.UserMessageLocalDataSource
import jed.choi.local.dao.UserMessageDao
import jed.choi.local.mapper.toUserMessageData
import jed.choi.local.mapper.toUserMessageLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserMessageLocalDataSourceImpl @Inject constructor(
    private val userMessageDao: UserMessageDao
) : UserMessageLocalDataSource {
    override fun getOldestUserMessage(): Flow<UserMessageData?> =
        userMessageDao.getOldest().map { it?.toUserMessageData() }.flowOn(Dispatchers.IO)


    override suspend fun getUserMessages(): List<UserMessageData> {
        return userMessageDao.getAll().map { it.toUserMessageData() }
    }

    override suspend fun saveUserMessage(userMessage: UserMessageData) {
        userMessageDao.add(userMessage.toUserMessageLocal())
    }

    override suspend fun removeUserMessage(id: Long) {
        userMessageDao.remove(id)
    }
}