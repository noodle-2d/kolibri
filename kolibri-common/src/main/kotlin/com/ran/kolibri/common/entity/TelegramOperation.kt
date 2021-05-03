package com.ran.kolibri.common.entity

import com.ran.kolibri.common.entity.telegram.operation.context.TelegramOperationContext
import org.joda.time.DateTime

data class TelegramOperation(
    val id: Long? = null,
    val operationName: String,
    val messageId: Int?,
    val context: TelegramOperationContext?,
    val createTime: DateTime
)
