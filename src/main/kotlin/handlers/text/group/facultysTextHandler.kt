package handlers.text

import backScope
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import kotlinx.coroutines.launch
import listFacs
import mongo.headings.getAll
import mongo.DataBase
import mongo.users.setFaculty
import mongo.users.setLastAction
import mongo.users.setMode
import utils.chat
import verifications.uId

fun Dispatcher.facultysDispather() = text {
    backScope.launch {
        val text = message.text!!
        val uId = message.from?.id!!
        if (listFacs.contains(text)) {
            DataBase.Users.setFaculty(uId.toString(), text)
            sendMessageWithHeading()
        }
    }
}

suspend fun TextHandlerEnvironment.sendMessageWithHeading(){
    DataBase.Users.setLastAction(uId, "FACULTYS")
    bot.sendMessage(
        chatId = chat(uId),
        text = "Выберите курс: ",
        replyMarkup = getKeyBoardHeading()
    )
}

suspend fun getKeyBoardHeading(): KeyboardReplyMarkup {
    val headings = DataBase.Heading.getAll().toMutableList()
    val newList = headings.windowed(3, 3, true) {
        it.map {
            KeyboardButton(
                it.name
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