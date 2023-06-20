package ktor.request

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import utils.userAgentList
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

val Wish: String
    get() = run {
        val url = URL("https://pozdravlala.ru/gen")
        val httpConn = url.openConnection() as HttpURLConnection
        httpConn.requestMethod = "POST"
        httpConn.setRequestProperty("authority", "pozdravlala.ru")
        httpConn.setRequestProperty("accept", "*/*")
        httpConn.setRequestProperty("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
        httpConn.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
        httpConn.setRequestProperty("cookie", "_ga=GA1.2.203428207.1664003814; _gid=GA1.2.808560230.1664003814; _gat=1")
        httpConn.setRequestProperty("origin", "https://pozdravlala.ru")
        httpConn.setRequestProperty("referer", "https://pozdravlala.ru/")
        httpConn.setRequestProperty(
            "sec-ch-ua",
            "\"Google Chrome\";v=\"105\", \"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"105\""
        )
        httpConn.setRequestProperty("sec-ch-ua-mobile", "?0")
        httpConn.setRequestProperty("sec-ch-ua-platform", "\"Linux\"")
        httpConn.setRequestProperty("sec-fetch-dest", "empty")
        httpConn.setRequestProperty("sec-fetch-mode", "cors")
        httpConn.setRequestProperty("sec-fetch-site", "same-origin")
        httpConn.setRequestProperty(
            "user-agent",
            userAgentList.random()
        )
        httpConn.doOutput = true
        val writer = OutputStreamWriter(httpConn.outputStream)
        writer.write("[0,0,0,0,0]")
        writer.flush()
        writer.close()
        httpConn.outputStream.close()
        val responseStream = if (httpConn.responseCode / 100 == 2) httpConn.inputStream else httpConn.errorStream
        val s = Scanner(responseStream).useDelimiter("\\A")
        val response = if (s.hasNext()) s.next() else ""
        val wishMap = Json.decodeFromString<Map<String, String>>(response)
        return wishMap["text"] ?: ""
    }