package ktor.request

import BASE_URL
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import ktor.Klient

suspend fun updateScheduleAudience(body: BodyParse): HttpResponse? {
    try {
        Klient.use {
            return it.post("${BASE_URL}updateScheduleAudience") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
    }catch (e: Exception){
        println(e)
        return null
    }
}