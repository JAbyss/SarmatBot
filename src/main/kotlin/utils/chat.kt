package utils

import com.github.kotlintelegrambot.entities.ChatId

fun chat(id: String) = ChatId.Id(id.toLong())
