package ktor.request

import BASE_URL
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import ktor.Klient
import java.lang.Exception

@Serializable
data class BodyParse(
    val groupId: String,
    val date: String,
    val mode: String = "FACULTY"
)

suspend fun parseSchedule(body: BodyParse): HttpResponse? {
    try {
        Klient.use {
            return it.post("${BASE_URL}parseSchedule") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
    } catch (e: Exception) {
        println(e)
        return null
    }
}

suspend fun parseScheduleAudience(body: BodyParse): HttpResponse? {
    try {
        Klient.use {
            return it.post("${BASE_URL}parseScheduleAudience") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
    } catch (e: Exception) {
        println(e)
        return null
    }
}