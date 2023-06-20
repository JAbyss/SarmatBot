package utils

import java.text.SimpleDateFormat
import java.util.*

private val formatFull = SimpleDateFormat("d MMM yyyy г. HH:mm:ss")
private val fullDate: String
    get() = run {
        formatFull.timeZone = TimeZone.getTimeZone("GMT+05")
        formatFull.format(Date())
    }

fun tprint(value: Any) = println("$fullDate $value")