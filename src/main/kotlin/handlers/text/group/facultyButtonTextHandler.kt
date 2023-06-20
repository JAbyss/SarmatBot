package handlers.text

import backScope
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import kotlinx.coroutines.launch
import mongo.facultys.getAll
import mongo.DataBase
import mongo.users.setLastAction
import mongo.users.setMode
import utils.chat
import verifications.isNotBlocked
import verifications.uId

fun Dispatcher.facultyButtonTextHandler() = text("Факультеты") {
    backScope.launch {
        isNotBlocked(uId){
//            DataBase.Users.setMode(uId, "FACULTY")
            sendMessageWithFacultys()
        }
    }
}

suspend fun TextHandlerEnvironment.sendMessageWithFacultys(){
    DataBase.Users.setLastAction(uId, "FACULTYS")
    bot.sendMessage(
        chatId = chat(uId),
        text = "Выберите факультет: ",
        replyMarkup = getKeyBoardFacultys()
    )
}

suspend fun getKeyBoardFacultys(): KeyboardReplyMarkup {
    val list = DataBase.Faculty.getAll()
    val newList = list.windowed(3, 3, true) {
        it.map {
            KeyboardButton(
                it.name
            )
        }
    }
    return KeyboardReplyMarkup(
        newList,
        resizeKeyboard = true,
        selective = true
    )
}