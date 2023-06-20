package server.routes

import bot
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utils.chat

fun Route.testRoute() = post("/testRoute") {

    val data = call.receiveText()

    bot.sendMessage(
        chatId = chat("468018135"),
        text = data,
        disableNotification = true
    )
    call.respond(HttpStatusCode.OK)
}