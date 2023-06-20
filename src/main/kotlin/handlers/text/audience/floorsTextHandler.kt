package handlers.text.audience

import backScope
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import kotlinx.coroutines.launch
import listFloors
import mongo.audiences.getAll
import mongo.DataBase
import mongo.users.getUserById
import mongo.users.setHeading
import mongo.users.setLastAction
import utils.chat
import verifications.isNotBlocked
import verifications.uId

fun Dispatcher.floorsTextDispatcher() = text {
    backScope.launch {
        isNotBlocked(uId) {
            if (listFloors.contains(text)) {
                sendMessageWithAudiences()
            }
        }
    }
}

suspend fun TextHandlerEnvironment.sendMessageWithAudiences() {
    DataBase.Users.setHeading(uId, text)
    DataBase.Users.setLastAction(uId, "FLOORS")
    bot.sendMessage(
        chatId = chat(uId),
        text = "Выберите аудиторию: ",
        replyMarkup = getKeyBoardAudiences(uId)
    )
}

suspend fun getKeyBoardAudiences(uId: String): KeyboardReplyMarkup {
    val user = DataBase.Users.getUserById(uId)!!
    val audiences = DataBase.Audience.getAll(user.faculty, user.heading)
    val _audiences = audiences.windowed(3, 3, true) {
        it.map {
            KeyboardButton(it.name)
        }
    }.toMutableList()
    _audiences.add(0, listOf(KeyboardButton("Назад")))
    return KeyboardReplyMarkup(
        _audiences,
        resizeKeyboard = true,
        selective = true
    )
}