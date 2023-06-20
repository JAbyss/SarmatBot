package handlers.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.entities.ParseMode
import mongo.DataBase
import mongo.changelog.getLastChangeLog
import utils.chat
import verifications.cCommand
import verifications.isNotBlocked
import verifications.uId

fun Dispatcher.commandLastUpdate() = cCommand("lastUpdate") {
    isNotBlocked(uId) {
        val lastLog = DataBase.ChangeLog.getLastChangeLog() ?: return@cCommand run {
            bot.sendMessage(
                chat(uId),
                text = "Изменений не найдено"
            )
        }
        bot.sendMessage(
            chat(uId),
            text = """
                |<b>Version: ${lastLog.version} || Дата: ${lastLog.date}</b>
                |
                |${lastLog.description}
            """.trimMargin(),
            parseMode = ParseMode.HTML
        )
    }
}