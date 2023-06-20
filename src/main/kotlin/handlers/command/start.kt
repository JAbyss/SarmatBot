package handlers.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import ktor.request.Wish
import mongo.DataBase
import mongo.users.clearInfoUser
import mongo.users.setGroup
import utils.chat
import verifications.cCommand
import verifications.isNBlockedU
import verifications.uId

fun Dispatcher.commandStart() = cCommand("start") {
    isNBlockedU(uId, message.from?.username ?: ""){
        var wish = Wish
        wish = if (wish.isEmpty()) "" else "\n$wish \n"
        DataBase.Users.clearInfoUser(uId)
        bot.sendSticker(
            chat(uId),
            sticker = "CAACAgIAAxkDAAIBFGMt6WR1JrOjj8m1o1Nxx389p67XAAKlFgAC3PlBSZ7_lly88iH3KQQ",
            replyMarkup = null
        )
        bot.sendMessage(
            chat(uId),
            text = """
            |Ну что ${message.from?.firstName} привет, что ли ✋
            |Я - бот Sarmat.
            |Я автоматизирую, как завещал нам  Henry Ford!
            |$wish
            |> Первый раз получение расписания занимает больше времени <
            |Выбери команду для продолжения или напиши /help.
        """.trimMargin(),
            replyMarkup = InlineKeyboardMarkup.create(
                listOf(
                    listOf(
                        InlineKeyboardButton.CallbackData("Факультеты", "\uD83C\uDF93 Факультеты"),
                        InlineKeyboardButton.CallbackData("Аудитории", "\uD83D\uDEAA Аудитории")
                    )
                ),
            )
        )
    }
}

