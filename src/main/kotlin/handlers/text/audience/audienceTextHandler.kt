package handlers.text.audience

import backScope
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.text
import handlers.text.sendDatesMessage
import kotlinx.coroutines.launch
import mongo.audiences.checkAudience
import mongo.DataBase
import mongo.users.setGroup
import mongo.users.setMode
import verifications.isNotBlocked
import verifications.uId

fun Dispatcher.audienceDispatcher() = text {
    backScope.launch {
        isNotBlocked(uId) {
            val idGroup = DataBase.Audience.checkAudience(message.text!!) ?: return@launch
            DataBase.Users.setMode(uId, "AUDIENCE")
            DataBase.Users.setGroup(uId, idGroup)
            sendDatesMessage(uId)
        }
    }
}