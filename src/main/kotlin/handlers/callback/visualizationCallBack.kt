package handlers.callback

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.handlers.CallbackQueryHandlerEnvironment
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import utils.chat
import verifications.cCallbackQuery
import verifications.uId

fun Dispatcher.visualizationCallBack() = cCallbackQuery("Визуализация") {
    bot.editMessageText(
        chatId = chat(uId),
        messageId = callbackQuery.message?.messageId,
        text = "Визуализация",
        replyMarkup = keyboardVis
    )
}

fun Dispatcher.sCallBack() = cCallbackQuery("Сокращать дисциплины") {
    bot.editMessageText(
        chatId = chat(uId),
        messageId = callbackQuery.message?.messageId,
        text = "Сокращать дисциплины",
        replyMarkup = keyboardOnOff
    )
}

fun Dispatcher.reductionFIOCallBack() = cCallbackQuery("Сокращать ФИО") {
    bot.editMessageText(
        chatId = chat(uId),
        messageId = callbackQuery.message?.messageId,
        text = "Сокращать ФИО",
        replyMarkup = keyboardReductionFIO
    )
}

fun Dispatcher.reductionAsyncLessonsCallBack() = cCallbackQuery("Скрывать асинхронные пары") {
    bot.editMessageText(
        chatId = chat(uId),
        messageId = callbackQuery.message?.messageId,
        text = "Скрывать асинхронные пары",
        replyMarkup = keyboardOnOff
    )
}

val keyboardVis = InlineKeyboardMarkup.create(
    listOf(
        listOf(
            InlineKeyboardButton.CallbackData("Сокращать дисциплины", "Сокращать дисциплины"),
        ),
        listOf(
            InlineKeyboardButton.CallbackData("Сокращать ФИО", "Сокращать ФИО"),
        ),
        listOf(
            InlineKeyboardButton.CallbackData("Скрывать асинхронные пары", "Скрывать асинхронные пары"),
        ),
        listOf(
            InlineKeyboardButton.CallbackData("Назад", "Назад"),
        )
    )
)

val keyboardOnOff = InlineKeyboardMarkup.create(
    listOf(
        listOf(
            InlineKeyboardButton.CallbackData("Включить", "Включить"),
            InlineKeyboardButton.CallbackData("Выключить", "Выключить")
        ),
        listOf(
            InlineKeyboardButton.CallbackData("Назад", "Назад")
        )
    )
)

fun CallbackQueryHandlerEnvironment.goToVisualization() {
    bot.editMessageText(
        chatId = chat(uId),
        messageId = callbackQuery.message?.messageId,
        text = "Визуализация",
        replyMarkup = keyboardVis
    )
}

val keyboardSettings = InlineKeyboardMarkup.create(
    listOf(
        listOf(
//            InlineKeyboardButton.CallbackData("Уведомления", "Уведомления"),
            InlineKeyboardButton.CallbackData("Визуализация", "Визуализация")
        ),
        listOf(
            //            InlineKeyboardButton.CallbackData("Уведомления", "Уведомления"),
            InlineKeyboardButton.CallbackData("Уведомления", "Уведомления")
        )
    )
)

val keyboardReductionFIO = InlineKeyboardMarkup.create(
    listOf(
        listOf(
            InlineKeyboardButton.CallbackData("Иванов И.И.", "reductOnlyName"),
        ),
        listOf(
            InlineKeyboardButton.CallbackData("Иванов Иван И.", "reductNameAndSecName"),
        ),
        listOf(
            InlineKeyboardButton.CallbackData("Иванов Иван Иванович", "reductNothing")
        ),
        listOf(
            InlineKeyboardButton.CallbackData("Назад", "Назад")
        )
    )
)

fun CallbackQueryHandlerEnvironment.goToSettings() {
    bot.editMessageText(
        chatId = chat(uId),
        messageId = callbackQuery.message?.messageId,
        text = "Настройки",
        replyMarkup = keyboardSettings
    )
}