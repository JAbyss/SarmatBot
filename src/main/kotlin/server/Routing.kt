package server

import io.ktor.server.application.*
import io.ktor.server.routing.*
import server.routes.notifyUserAboutUpdateSchedules
import server.routes.testRoute

fun Application.configureRouting() {

    routing {
        notifyUserAboutUpdateSchedules()
        testRoute()
    }
}