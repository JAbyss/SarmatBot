package handlers.callback

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.handlers.CallbackQueryHandlerEnvironment
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import utils.chat
import verifications.cCallbackQuery
import verifications.uId

fun Dispatcher.notificationCallBack() = cCallbackQuery("Уведомления") {
    bot.editMessageText(
        chatId = chat(uId),
        messageId = callbackQuery.message?.messageId,
        text = "Уведомления",
        replyMarkup = keyboardNotifications
    )
}

fun Dispatcher.updateNotificationCallBack() = cCallbackQuery("Изменение расписания") {
    bot.editMessageText(
        chatId = chat(uId),
        messageId = callbackQuery.message?.messageId,
        text = "Изменение расписания",
        replyMarkup = keyboardOnOff
    )
}

val keyboardNotifications = InlineKeyboardMarkup.create(
    listOf(
        listOf(
            InlineKeyboardButton.CallbackData("Изменение расписания", "Изменение расписания"),
            ),
        listOf(
            InlineKeyboardButton.CallbackData("Назад", "Назад"),
            )
    )
)

fun CallbackQueryHandlerEnvironment.goToNotifications() {
    bot.editMessageText(
        chatId = chat(uId),
        messageId = callbackQuery.message?.messageId,
        text = "Уведомления",
        replyMarkup = keyboardNotifications
    )
}