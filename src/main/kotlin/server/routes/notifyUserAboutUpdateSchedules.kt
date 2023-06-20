package server.routes

import bot
import com.github.kotlintelegrambot.entities.ParseMode
import handlers.text.generateMessage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import mongo.DataBase
import mongo.OneLessonDc
import mongo.audiences.getById
import mongo.groups.getById
import mongo.users.getAll
import utils.chat

@Serializable
data class UpdatedScheduleNotifyDc(
    val id: String,
    val date: String,
    val mode: String,
    val newSchedule: List<List<OneLessonDc>>
)

fun Route.notifyUserAboutUpdateSchedules() = post("/notifyUserAboutUpdateSchedules") {

    val data = call.receive<UpdatedScheduleNotifyDc>()

    val users = DataBase.Users.getAll()

    users.filter {
        it.idGroup == data.id && it.notifyUpdate
    }.forEach { user ->
        val name = when (data.mode) {
            "FACULTY" -> {
                DataBase.Groups.getById(data.id)?.name
            }

            "AUDIENCE" -> {
                DataBase.Audience.getById(data.id)?.name
            }
            else -> null
        }
        if (name != null) {
            val message = generateMessage(name, data.date, data.newSchedule, user, true)
            bot.sendMessage(
                chatId = chat(user.id),
                text = message,
                disableNotification = true,
                parseMode = ParseMode.HTML
            )
        }
    }
    call.respond(HttpStatusCode.OK)
}