package handlers.text.group

import backScope
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import kotlinx.coroutines.launch
import listHeadings
import mongo.groups.getGroupsByFilters
import mongo.DataBase
import mongo.users.getUserById
import mongo.users.setHeading
import mongo.users.setLastAction
import utils.chat
import verifications.uId

fun Dispatcher.headingsDispatcher() = text {
    val text = message.text!!
    val id = message.from?.id!!
    backScope.launch {
        val _listHeadings = listHeadings.filter { it.name == text }
        if (_listHeadings.isNotEmpty()) {
            DataBase.Users.setHeading(id.toString(), _listHeadings[0].id)
            sendMessageWithGroup()
        }
    }
}

suspend fun TextHandlerEnvironment.sendMessageWithGroup(){
    DataBase.Users.setLastAction(uId, "HEADING")
    bot.sendMessage(
        chatId = chat(uId),
        text = "Выберите группу: ",
        replyMarkup = getKeyBoardGroups(uId)
    )
}

suspend fun getKeyBoardGroups(uId: String): KeyboardReplyMarkup {
    val user = DataBase.Users.getUserById(uId)!!
    val groups = DataBase.Groups.getGroupsByFilters(user.faculty, user.heading)
    val newList = groups.windowed(1, 1, true) {
        it.map {
            KeyboardButton(
                if (it.name == "ПРО-137Б") "⚜️ ${it.name}" else it.name
            )
        }
    }.toMutableList()
    newList.add(0, listOf(KeyboardButton("Назад")))
    return KeyboardReplyMarkup(
        newList,
        resizeKeyboard = true,
        selective = true
    )
}