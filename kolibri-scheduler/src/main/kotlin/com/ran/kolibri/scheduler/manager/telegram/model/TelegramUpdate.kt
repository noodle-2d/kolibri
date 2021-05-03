package com.ran.kolibri.scheduler.manager.telegram.model

interface TelegramUpdate

data class UnknownChatUpdate(val chatId: Int?) : TelegramUpdate

interface OperationUpdate : TelegramUpdate

data class OperationInitiation(val type: TelegramOperationType) : OperationUpdate

interface OperationContinuation : OperationUpdate

data class CallbackQuery(val messageId: Int, val data: String) : OperationContinuation

data class PlainText(val text: String) : OperationContinuation
