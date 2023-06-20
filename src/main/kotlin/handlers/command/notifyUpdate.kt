package handlers.command

import Admins
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.entities.ParseMode
import mongo.DataBase
import mongo.users.getAll
import utils.chat
import utils.tprint
import verifications.cCommand
import verifications.isNotBlocked
import verifications.uId

fun Dispatcher.commandNotifyUpdate() = cCommand("notifyUpdate") {
    isNotBlocked(uId) {
        if (Admins.contains(uId)) {
            val allUsers = DataBase.Users.getAll()
            allUsers.forEach { user ->
                val result =
                    bot.sendMessage(
                    chat(user.id),
                    text = """
                |Я обновился
                |Изменения тут -> /lastUpdate
            """.trimMargin(),
                    parseMode = ParseMode.HTML,
                    disableNotification = true
                )
                result.fold(
                    ifSuccess = {},
                    ifError = { tprint("Error send notifyUpdate idUser: ${user.id}") }
                )
            }
        }
    }
}