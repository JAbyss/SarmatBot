package handlers.text.audience

import backScope
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.handlers.TextHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import mongo.buildings.getAll
import kotlinx.coroutines.launch
import mongo.DataBase
import mongo.users.setLastAction
import mongo.users.setMode
import utils.chat
import verifications.isNotBlocked
import verifications.uId

fun Dispatcher.audienceButtonTextDispatcher() = text("Аудитории") {
    backScope.launch {
        isNotBlocked(uId){
//            DataBase.Users.setMode(uId, "AUDIENCE")
            sendMessageWithBuildings()
        }
    }
}

suspend fun TextHandlerEnvironment.sendMessageWithBuildings(){
    DataBase.Users.setLastAction(uId, "AUDIENCE")
    bot.sendMessage(
        chatId = chat(uId),
        text = "Выберите здание: ",
        replyMarkup = getKeyBoardBuildings()
    )
}

suspend fun getKeyBoardBuildings(): KeyboardReplyMarkup {
    val buildings = DataBase.Buildings.getAll()
    val newList = buildings.windowed(3, 3, true) {
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