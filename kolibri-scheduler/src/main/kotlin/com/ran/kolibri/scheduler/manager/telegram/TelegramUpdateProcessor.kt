package com.ran.kolibri.scheduler.manager.telegram

import com.ran.kolibri.common.entity.TelegramOperation
import com.ran.kolibri.common.util.log
import com.ran.kolibri.scheduler.manager.telegram.model.OperationInitiation
import com.ran.kolibri.scheduler.manager.telegram.model.OperationUpdate
import com.ran.kolibri.scheduler.manager.telegram.model.TelegramOperationType
import javax.naming.OperationNotSupportedException

interface TelegramUpdateProcessor {
    val operationType: TelegramOperationType
    suspend fun processUpdate(operation: TelegramOperation, update: OperationUpdate): TelegramOperation
}

interface SingleActionUpdateProcessor : TelegramUpdateProcessor {

    suspend fun doProcessUpdate()

    override suspend fun processUpdate(operation: TelegramOperation, update: OperationUpdate): TelegramOperation =
        if (update is OperationInitiation && update.type == operationType) {
            doProcessUpdate()
            operation
        } else IgnoreUpdateProcessor.processUpdate(operation, update)
}

object IgnoreUpdateProcessor : TelegramUpdateProcessor {
    override val operationType: Nothing
        get() = throw OperationNotSupportedException()

    override suspend fun processUpdate(operation: TelegramOperation, update: OperationUpdate): TelegramOperation {
        log.info("Ignoring processing unknown update $update")
        return operation
    }
}
