package handlers.text

import backScope
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.text
import handlers.text.audience.sendMessageWithBuildings
import handlers.text.audience.sendMessageWithFloors
import handlers.text.group.sendMessageWithGroup
import kotlinx.coroutines.launch
import mongo.DataBase
import mongo.users.getLastAction
import verifications.isNotBlocked
import verifications.uId

fun Dispatcher.backDispatcher() = text("Назад") {
    backScope.launch {
        isNotBlocked(uId) {
            when (DataBase.Users.getLastAction(uId)) {
                "FACULTYS" -> {
                    sendMessageWithFacultys()
                }

                "HEADING" -> {
                    sendMessageWithHeading()
                }

                "GROUPS" -> {
                    sendMessageWithGroup()
                }

                "AUDIENCE" -> {
                    sendDatesMessage(uId)
                }

                "BUILDINGS" -> {
                    sendMessageWithBuildings()
                }
                "FLOORS" -> {
                    sendMessageWithFloors()
                }
            }
        }
    }
}