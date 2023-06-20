package handlers.text.audience

import backScope
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import kotlinx.coroutines.launch
import listBuildings
import mongo.floors.getAll
import mongo.DataBase
import mongo.users.setFaculty
import mongo.users.setLastAction
import utils.chat
import verifications.isNotBlocked
import verifications.uId

fun Dispatcher.buildingTextDispatcher() = text {
    backScope.launch {
        isNotBlocked(uId){
            if (listBuildings.contains(text)){
                sendMessageWithFloors()
            }
        }
    }
}

suspend fun TextHandlerEnvironment.sendMessageWithFloors(){
    DataBase.Users.setFaculty(uId, text)
    DataBase.Users.setLastAction(uId, "BUILDINGS")
    bot.sendMessage(
        chatId = chat(uId),
        text = "Выберите этаж: ",
        replyMarkup = getKeyBoardFloors()
    )
}

suspend fun getKeyBoardFloors(): KeyboardReplyMarkup {
    val floors = DataBase.Floors.getAll()
    val _floors = floors.windowed(3,3, true){
        it.map {
            KeyboardButton(it.name)
        }
    }.toMutableList()
    _floors.add(0, listOf(KeyboardButton("Назад")))
    return KeyboardReplyMarkup(
        _floors,
        resizeKeyboard = true,
        selective = true
    )
}