package handlers.text

import ServerDate.fullDate
import backScope
import bot
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.User
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import com.github.kotlintelegrambot.network.fold
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import ktor.request.BodyParse
import ktor.request.parseSchedule
import ktor.request.parseScheduleAudience
import ktor.request.updateScheduleAudience
import listDates
import mongo.*
import mongo.logs.addGotSchedule
import mongo.schedule.getByDateAndIdGroup
import mongo.users.*
import org.litote.kmongo.eq
import utils.chat
import verifications.IdUser
import verifications.isNotBlocked
import verifications.uId

val regex = "(?=Пон-ник)|(?=Вторник)|(?=Среда)|(?=Четверг)|(?=Пятница)|(?=Суббота)".toRegex()
val regexDate = "^\\d{2}.\\d{2}.\\d{4}\$".toRegex()
fun Dispatcher.dateHandler() = text {
    backScope.launch {
        isNotBlocked(uId) {
            val mess = message.text!!

            if (regexDate.containsMatchIn(mess)) {
                sendSchedule(mess, uId)
            } else if (regex.containsMatchIn(mess)) {

                val nowDate = listDates.filter {
                    it.weekAndDay == mess
                }
                if (nowDate.isNotEmpty()) {
                    val date = nowDate[0].date
                    sendSchedule(date, uId)
                } else {
                    val date = listDates[0].date
                    sendSchedule(date, uId)
                }
            }
        }
    }
}

suspend fun sendSchedule(date: String, idUser: IdUser) {
    val user = DataBase.Users.getUserById(idUser)!!
    val idGroup = user.idGroup
    val nameGroup =
        DataBase.Groups.getCollection().findOne(GroupDC::id eq idGroup)?.name ?: DataBase.Audience.getCollection()
            .findOne(AudienceDC::id eq idGroup)?.name ?: return run {
            DataBase.Users.clearInfoUser(idUser)
            bot.sendMessage(
                chat(idUser),
                text = """
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
    var isNewSchedule = false
    val schedule = DataBase.Schedule.getByDateAndIdGroup(date, idGroup)?.listLessons ?: run {
        isNewSchedule = true
        val response = if (user.mode == "FACULTY")
            parseSchedule(BodyParse(date = date, groupId = idGroup, mode = "FACULTY"))
        else
            parseScheduleAudience(BodyParse(date = date, groupId = idGroup, mode = "AUDIENCE"))
        if (response != null)
            if (response.status.isSuccess()) {
                return@run response.body<List<List<OneLessonDc>>>()
            } else run {
                bot.sendMessage(
                    chat(idUser),
                    text = "Расписание отсутствует",
                    replyMarkup = getKeyBoardToday()
                )
                return
            }
        else run {
            bot.sendMessage(
                chat(idUser),
                text = "Внутренний сбой",
                replyMarkup = getKeyBoardToday()
            )
            println("Внутренний сбой: запрос")
            bot.sendMessage(
                chat("468018135"),
                text = "Внутренний сбой",
                replyMarkup = getKeyBoardToday()
            )
            return
        }
    }
    val fullString = generateMessage(nameGroup, date, schedule, user)
    val message = bot.sendMessage(
        chat(idUser),
        text = fullString,
        parseMode = ParseMode.HTML,
        replyMarkup = getKeyBoardToday()
    )
    message.fold(
        ifSuccess = {
            DataBase.Users.setLastMessageId(idUser, it.messageId.toString())
            addGotSchedule(uId = idUser)
        },
        ifError = {
            println("$fullDate Message with Schedule error")
        }
    )
    backScope.launch {
        if (!isNewSchedule) {
            val lastMessageId = DataBase.Users.getLastMessageId(idUser) ?: return@launch
            val response = updateScheduleAudience(
                BodyParse(
                    date = date,
                    groupId = idGroup,
                    mode = user.mode
                )
            )
            if (response != null) {
                if (response.status.isSuccess()) {
                    val result = bot.editMessageText(
                        chat(idUser),
                        lastMessageId.toLong(),
                        text = generateMessage(nameGroup, date, response.body(), user),
                        parseMode = ParseMode.HTML
                    )
                    result.fold(
                        response = {
                            backScope.launch {
                                addGotSchedule(uId = idUser)
                            }
                        }
                    )
                }
            } else {
                bot.sendMessage(
                    chat(idUser),
                    text = "Внутренний сбой",
                    replyMarkup = getKeyBoardToday()
                )
                println("Внутренний сбой: запрос")
                bot.sendMessage(
                    chat("468018135"),
                    text = "Внутренний сбой",
                    replyMarkup = getKeyBoardToday()
                )
            }
        }
    }
}

fun formatTeachers(teachers: List<String>, reductionFIO: ReductionFIO): List<String> {
    return when (reductionFIO) {
        ReductionFIO.SecondN_FN_TN -> {
            teachers.map {
                val FIO = it.split(" ")
                if (FIO.size > 1) {
                    FIO.reduceIndexed { i, acc, s ->
                        if (i == 1)
                            acc + " " + s[0] + "."
                        else
                            acc + s[0] + "."
                    }
                } else {
                    it
                }
            }
        }

        ReductionFIO.SecondN_FirstN_TH -> {
            teachers.map {
                val FIO = it.split(" ")
                if (FIO.size > 2) {
                    FIO[0] + " " + FIO[1] + " " + FIO[2][0] + "."
                } else {
                    it
                }
            }
        }

        ReductionFIO.Full -> {
            teachers
        }
    }
}

fun formatLessons(oneLesson: OneLessonDc, reductionMessage: Boolean): String {
    return if (reductionMessage) {
        val parts = oneLesson.name.replace("-", " ").split(" ")
        if (parts.size > 1) {
            parts.reduceIndexed { i, c, n ->
                c + " " + n.take(4) + if (n.length <= 3) "" else "."
            }
        } else {
            oneLesson.name
        }
    } else {
        oneLesson.name
    }
}

fun formatAsyncLessons(listLesson: List<OneLessonDc>, reductionAsyncLessons: Boolean, actionAsync: () -> Unit): List<OneLessonDc> {
    return listLesson.mapNotNull {
        if (reductionAsyncLessons) {
            if (it.cab == "Асинхронное обучение") {
                actionAsync()
                null
            } else
                it
        } else if (it.cab == "Асинхронное обучение") {
            it.copy(cab = "Async")
        } else
            it
    }
}

fun generateMessageWithOneLesson(lesson: OneLessonDc, user: UsersDC): String {
    if (lesson.name.isNotEmpty()) {
        val formatted = if (lesson.cab == "Асинхронное обучение") "Async" else lesson.cab
        val teachers = lesson.prepod.split("\n")

        val formattedTeachers = formatTeachers(teachers, user.reductionFIO)

        val teachersString = if (formattedTeachers.size > 1) {
            formattedTeachers.joinToString(separator = "") {
                if (formattedTeachers.indexOf(it) != formattedTeachers.lastIndex) {
                    "├      <code>$it</code>".trimIndent()
                } else
                    "\n└      <code>$it</code>".trimEnd()
            }
        } else {
            if (formattedTeachers[0].isNotEmpty())
                "└      <code>${formattedTeachers[0]}</code>".trimEnd()
            else
                ""
        }
        val nameLesson = formatLessons(lesson, user.reductionMessage)
        return """ 
            |⏳ <CODE>${lesson.time}</CODE>
            |┌─────────────────┐
            |├   <b>${lesson.type}</b>
            |${if (teachersString.isNotEmpty()) "├" else "└"}      $nameLesson          
            |$teachersString ${if (lesson.prepod.isEmpty()) "" else "\n"}
            | (<code>${formatted}</code>)
            | 
        """.trimMargin()
    }
    return ""
}

fun generateMessageWithSomeAnyLessons(listLesson: List<OneLessonDc>, user: UsersDC): String {
    val rawTeachers = listLesson.map { it.prepod }
    val formattedTeachers = formatTeachers(rawTeachers, user.reductionFIO)
    val teachersWithHTML = formattedTeachers.map {
        "<code>$it</code>"
    }

    val fullStringForTwicedAnyLessons =
        if (listLesson.size == 1) listLesson[0].name else {
//            val formattedList = formatAsyncLessons(listLesson, user.reductionAsyncLessons)
            listLesson.mapIndexed { i, v ->
                val formattedLessons = formatLessons(v, user.reductionMessage)
                "├   <b>" + v.type + "</b>" + "\n" +
                        "├      " + formattedLessons + " " + "<code>(${if (v.cab == "Асинхронное обучение") "Async" else v.cab})</code>" +
                        if (i != listLesson.lastIndex)
                            "\n├      ${teachersWithHTML[i]}\n├─────────────────┤"
                        else {
                            "\n└      ${teachersWithHTML[i]}\n"
                        }
            }.joinToString(separator = "\n")
        }

    return """ 
            |⏳ <CODE>${listLesson[0].time}</CODE>
            |┌─────────────────┐
            |$fullStringForTwicedAnyLessons         
            |
            | 
        """.trimMargin()
}

fun generateMessageWithSomeEqualLessons(listLesson: List<OneLessonDc>, user: UsersDC): String {
    if (listLesson[0].name.isNotEmpty()) {
        val rawTeachers = listLesson.map { it.prepod }
        val formattedTeachers = formatTeachers(rawTeachers, user.reductionFIO)
        val teachersWithHTML = formattedTeachers.map {
            "<code>$it</code>"
        }

        var readyTeacherString = ""

        readyTeacherString += "└      ${teachersWithHTML[0]}\n"

        val formatted =
            if (listLesson[0].cab == "Асинхронное обучение") "Async" else listLesson.joinToString("") { it.cab }


        val nameLesson = formatLessons(listLesson[0], user.reductionMessage)

        return """ 
            |⏳ <CODE>${listLesson[0].time}</CODE>
            |┌─────────────────┐
            |├   <b>${listLesson[0].type}</b> (${listLesson.size}-я)
            |${if (listLesson[0].prepod.isNotEmpty()) "├" else "└"}      $nameLesson         
            |$readyTeacherString
            | (<code>${formatted}</code>)
            | 
        """.trimMargin()
    }
    return ""
}

fun generateMessage(nameGroup: String, date: String, schedule: List<List<OneLessonDc>>, user: UsersDC, isScheduleUpdated: Boolean = false): String {
    val strings = mutableListOf(if (isScheduleUpdated) "Обновленное расписание $nameGroup на $date.\n" else "Расписание $nameGroup на $date.\n")
    var countAsyncLessons = 0
    schedule.forEach { listLesson ->
        val formattedList = formatAsyncLessons(listLesson, user.reductionAsyncLessons){
            countAsyncLessons++
        }

        formattedList.forEach { _ ->
            if (listLesson.size == 1) {
                strings.add(generateMessageWithOneLesson(formattedList[0], user))
            } else {
                var all = false
                listLesson.reduce { c, v ->
                    if (v.name.replace("-", " ") == c.name.replace(
                            "-",
                            " "
                        ) && c.prepod == v.prepod && c.type == v.type
                    )
                        all = true
                    else {
                        all = false
                        return@reduce v
                    }
                    v
                }
                strings.add(
                    if (all) {
                        generateMessageWithSomeEqualLessons(formattedList, user)
                    } else
                        generateMessageWithSomeAnyLessons(formattedList, user)
                )
            }
        }
    }
    if (countAsyncLessons != 0){
        strings.add("""
            |Количество асинхронных пар: $countAsyncLessons
        """.trimMargin())
    }
    return strings.joinToString("\n")
}

fun getKeyBoardToday(): KeyboardReplyMarkup {
    val list = listDates.windowed(3, 3, true) {
        it.map { KeyboardButton(it.weekAndDay) }
    }.toMutableList()
    list.add(
        listOf(
            KeyboardButton("\uD83C\uDF93\nФакультеты"),
            KeyboardButton("\uD83D\uDEAA\nАудитории")
        )
    )
    return KeyboardReplyMarkup(
        list,
        resizeKeyboard = true,
        selective = true
    )
}

fun sendDatesMessage(idUser: String) {
    bot.sendMessage(
        chat(idUser),
        text = "Расписание: ",
        replyMarkup = getKeyBoardToday()
    )
}