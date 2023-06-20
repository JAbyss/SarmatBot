package handlers.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import handlers.callback.keyboardSettings
import utils.chat
import verifications.cCommand
import verifications.isNBlockedU
import verifications.uId

fun Dispatcher.commandSettings() = cCommand("settings") {
    isNBlockedU(uId, message.from?.username ?: "") {
        bot.sendMessage(
            chatId = chat(uId),
            text = "Настройки",
            replyMarkup = keyboardSettings
        )
    }
}

