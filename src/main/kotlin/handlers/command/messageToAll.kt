package handlers.command

import Admins
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.entities.ChatId
import mongo.DataBase
import mongo.users.getAll
import verifications.cCommand
import verifications.uId

fun Dispatcher.messageToAll() = cCommand("messageToAll") {
    if (Admins.contains(uId)) {
        val message = args.joinToString(separator = " ")
        val allUsers = DataBase.Users.getAll()
        allUsers.forEach { user ->
            val result = bot.sendMessage(
                ChatId.fromId(user.id.toLong()),
                message
            )
            result.fold(
                ifSuccess = {},
                ifError = { println("Error send message idUser: ${user.id}") }
            )
        }
    }
}