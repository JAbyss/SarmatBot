package handlers.text.group

import backScope
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.text
import handlers.text.sendDatesMessage
import kotlinx.coroutines.launch
import mongo.groups.checkGroup
import mongo.DataBase
import mongo.users.setGroup
import mongo.users.setMode
import verifications.isNotBlocked
import verifications.uId

fun Dispatcher.groupHandler() {
    text {
        backScope.launch {
            isNotBlocked(uId) {
                val idGroup = DataBase.Groups.checkGroup(message.text!!.replace("⚜️ ", "")) ?: return@launch
                DataBase.Users.setMode(uId, "FACULTY")
                DataBase.Users.setGroup(uId, idGroup)
                sendDatesMessage(uId)
            }
        }
    }
}