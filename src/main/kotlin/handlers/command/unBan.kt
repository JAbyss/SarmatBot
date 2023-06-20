package handlers.command

import Admins
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.entities.ChatId
import mongo.DataBase
import mongo.users.unbanUser
import verifications.cCommand
import verifications.uId

fun Dispatcher.commandUnBan() = cCommand("unBan") {
    try {
        if (Admins.contains(uId)) {
            val idUserForBan = args[0]
            DataBase.Users.unbanUser(idUserForBan)
            bot.sendMessage(
                ChatId.fromId(idUserForBan.toLong()),
                text = "Вы разбанены"
            )
        }
    } catch (e: java.lang.Exception) {
        println(e.toString())
    }
}