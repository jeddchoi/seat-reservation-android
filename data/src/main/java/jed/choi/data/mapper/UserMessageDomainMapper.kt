package jed.choi.data.mapper

import jed.choi.data.model.UserMessageData
import jed.choi.domain.entity.UserMessageEntity

fun UserMessageData.toUserMessageEntity() = UserMessageEntity(id, message)

fun UserMessageEntity.toUserMessageData() = UserMessageData(id, message)