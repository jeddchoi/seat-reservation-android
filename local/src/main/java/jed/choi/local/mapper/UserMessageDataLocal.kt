package jed.choi.local.mapper

import jed.choi.data.model.UserMessageData
import jed.choi.local.model.UserMessageLocal

fun UserMessageLocal.toUserMessageData() = UserMessageData(id, message)

fun UserMessageData.toUserMessageLocal() = UserMessageLocal(id, message)