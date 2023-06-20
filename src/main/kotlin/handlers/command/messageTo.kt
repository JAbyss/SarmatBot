package handlers.command

import Admins
import backScope
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.launch
import verifications.cCommand
import verifications.uId

fun Dispatcher.messageTo() = cCommand("messageTo") {
    if (Admins.contains(uId)) {
        val idUser = args[0]
        val message = args.drop(1).joinToString(separator = " ")

        val result = bot.sendMessage(
            ChatId.fromId(idUser.toLong()),
            message
        )
        result.fold(
            ifSuccess = {},
            ifError = { println("Error send message idUser: $idUser") }
        )
    }
}