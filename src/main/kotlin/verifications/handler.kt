package verifications

import backScope
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.CallbackQueryHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text
import kotlinx.coroutines.launch
import mongo.DataBase
import mongo.logs.addRegisteredUser
import mongo.users.getUserById
import mongo.users.insertUser

typealias IdUser = String
typealias Message = String

fun Dispatcher.cCommand(name: String, body: suspend CommandHandlerEnvironment.() -> Unit) {
    command(name) {
        backScope.launch {
                body()
        }
    }
}

fun Dispatcher.cText(name: String, body: suspend TextHandlerEnvironment.() -> Unit) {
    text(name) {
        backScope.launch {
            isNotBlocked(uId) {
                body()
            }
        }
    }
}

fun Dispatcher.cCallbackQuery(name: String, body: suspend CallbackQueryHandlerEnvironment.() -> Unit){
    callbackQuery(name){

        backScope.launch {
            isNotBlocked(uId){
                body()
            }
        }
    }
}

suspend inline fun isNotBlocked(idUser: String, action: () -> Unit) {
    val user = DataBase.Users.getUserById(idUser) ?: run {
        DataBase.Users.insertUser(idUser)
        return action()
    }
    if (!user.isBanned) {
        action()
    }
}

suspend inline fun isNBlockedU(idUser: String, username: String, action: () -> Unit) {
    val user = DataBase.Users.getUserById(idUser) ?: run {
        DataBase.Users.insertUser(idUser, username)
        return action()
    }
    if (!user.isBanned) {
        action()
    }
}

val CommandHandlerEnvironment.uId: String
    get() = message.from?.id.toString()

val CommandHandlerEnvironment.mId: String
    get() = message.messageId.toString()

val TextHandlerEnvironment.uId: String
    get() = message.from?.id.toString()

val TextHandlerEnvironment.mId: String
    get() = message.messageId.toString()

val CallbackQueryHandlerEnvironment.uId: String
    get() = callbackQuery.from.id.toString()

val CallbackQueryHandlerEnvironment.cId: String
    get() = callbackQuery.id.toString()