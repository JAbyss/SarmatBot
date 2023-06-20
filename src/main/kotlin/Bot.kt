import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import handlers.callback.*
import handlers.command.*
import handlers.text.*
import handlers.text.audience.*
import handlers.text.backDispatcher
import handlers.text.group.groupHandler
import handlers.text.group.headingsDispatcher
import mongo.DataBase
import mongo.ReductionFIO
import mongo.users.*
import mongo.users.*
import mongo.users.*
import mongo.users.*
import utils.chat
import utils.h
import utils.m
import utils.s
import verifications.cCallbackQuery
import verifications.uId
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

object ServerDate {
    private val formatFull = SimpleDateFormat("d MMM yyyy г. HH:mm:ss")
    private val formatHours = SimpleDateFormat("HH")
    private val formatDaysForParse = SimpleDateFormat("dd.MM.yyyy")
    private val formatHoursAndMinutes = SimpleDateFormat("HH:mm:ss")


    val fullDate: String
        get() = run {
            formatFull.timeZone = TimeZone.getTimeZone("GMT+05")
            formatFull.format(Date())
        }

    val hour: String
        get() = run {
            formatHours.timeZone = TimeZone.getTimeZone("GMT+05")
            formatHours.format(Date())
        }

    val hourAndMinutes: String
        get() = run {
            formatHoursAndMinutes.timeZone = TimeZone.getTimeZone("GMT+05")
            formatHoursAndMinutes.format(Date())
        }

    val date: String
        get() = run {
            formatDaysForParse.timeZone = TimeZone.getTimeZone("GMT+05")
            formatDaysForParse.format(Date())
        }
    val timeToEndDay: Long
        get() = run {
            val values = hourAndMinutes.split(":")

            val h = values[0].toInt().h
            val m = values[1].toInt().m
            val s = values[2].toInt().s

            val delay = 24.h - (h + m + s)
            delay
        }
}

val bot = bot {
// TEST   5663736314:AAEtdj42mkZ6z8E5vO5NGvyj6D0mUyF0sUE
// PROD   5357238112:AAGbESUq9D34WLqRV5FyEVw0vtE09IpSghM
    token = "5357238112:AAGbESUq9D34WLqRV5FyEVw0vtE09IpSghM"
    dispatch {
        try {
            cCallbackQuery("\uD83C\uDF93 Факультеты") {
                DataBase.Users.setMode(uId, "FACULTY")
                bot.sendMessage(
                    chat(uId),
                    text = "Выберите факультет: ",
                    replyMarkup = getKeyBoardFacultys()
                )
            }
            cCallbackQuery("\uD83D\uDEAA Аудитории") {
                DataBase.Users.setMode(uId, "AUDIENCE")
                bot.sendMessage(
                    chatId = chat(uId),
                    text = "Выберите аудиторию: ",
                    replyMarkup = getKeyBoardBuildings()
                )
            }
            cCallbackQuery("Включить") {
                val parameter = callbackQuery.message?.text
                val state = callbackQuery.data
                when (parameter) {
                    "Сокращать дисциплины" -> {
                        DataBase.Users.setReductionLesson(uId, true)
                        goToVisualization()
                    }

                    "Скрывать асинхронные пары" -> {
                        DataBase.Users.setReductionAsyncLessons(uId, true)
                        goToVisualization()
                    }

                    "Изменение расписания" -> {
                        DataBase.Users.setNotifyUpdateSchedule(uId, true)
                        goToNotifications()
                    }
                }
            }
            cCallbackQuery("Выключить") {
                val parameter = callbackQuery.message?.text
                val state = callbackQuery.data
                when (parameter) {
                    "Сокращать дисциплины" -> {
                        DataBase.Users.setReductionLesson(uId, false)
                        goToVisualization()
                    }

                    "Скрывать асинхронные пары" -> {
                        DataBase.Users.setReductionAsyncLessons(uId, false)
                        goToVisualization()
                    }

                    "Изменение расписания" -> {
                        DataBase.Users.setNotifyUpdateSchedule(uId, false)
                        goToNotifications()
                    }
                }
            }
            cCallbackQuery("Назад") {
                val parameter = callbackQuery.message?.text
                val state = callbackQuery.data
                when (parameter) {
                    "Сокращать дисциплины" -> {
                        goToVisualization()
                    }

                    "Визуализация" -> {
                        goToSettings()
                    }

                    "Сокращать ФИО" -> {
                        goToVisualization()
                    }

                    "Скрывать асинхронные пары" -> {
                        goToVisualization()
                    }
                    "Уведомления" -> {
                        goToSettings()
                    }
                    "Изменение расписания" -> {
                        goToNotifications()
                    }
                }
            }
            cCallbackQuery("reductOnlyName") {
                DataBase.Users.setReductionFIO(uId, ReductionFIO.SecondN_FN_TN)
                goToVisualization()
            }
            cCallbackQuery("reductNameAndSecName") {
                DataBase.Users.setReductionFIO(uId, ReductionFIO.SecondN_FirstN_TH)
                goToVisualization()
            }
            cCallbackQuery("reductNothing") {
                DataBase.Users.setReductionFIO(uId, ReductionFIO.Full)
                goToVisualization()
            }
            visualizationCallBack()
            sCallBack()
            reductionFIOCallBack()
            reductionAsyncLessonsCallBack()
            notificationCallBack()
            updateNotificationCallBack()

            commandStart()
            commandHelp()
            commandBan()
            messageTo()
            messageToAll()
            commandUnBan()
            commandSettings()
            commandCommands()
            commandLastUpdate()
            commandNotifyUpdate()

            groupHandler()
            dateHandler()
            facultysDispather()
            headingsDispatcher()
            facultyButtonTextHandler()
            backDispatcher()
            audienceButtonTextDispatcher()
            buildingTextDispatcher()
            floorsTextDispatcher()
            audienceDispatcher()
        } catch (e: Exception) {
            println(e)
        }
    }
}

data class DateDC(
    val weekAndDay: String,
    val date: String
)

fun getDaysOnWeek(): MutableList<DateDC> {
    val mapWeeks = mapOf(
        "MONDAY" to "Пон-ник",
        "TUESDAY" to "Вторник",
        "WEDNESDAY" to "Среда",
        "THURSDAY" to "Четверг",
        "FRIDAY" to "Пятница",
        "SATURDAY" to "Суббота"
    )
    val _listDates = mutableListOf<DateDC>()
    val date = LocalDate.parse(ServerDate.date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    var newDate = if (date.dayOfWeek.name == "SUNDAY")
        date.plusDays(1)
    else
        date
    val item = DateDC(
        weekAndDay = mapWeeks[newDate.dayOfWeek.name] + " " + newDate.dayOfMonth,
        date = newDate.toString().split("-").reversed().joinToString(separator = ".")
    )
    _listDates.add(item)
    repeat(5) {
        newDate += if (newDate.dayOfWeek.name == "SATURDAY") {
            Period.ofDays(2)
        } else {
            Period.ofDays(1)
        }
        val item = DateDC(
            weekAndDay = mapWeeks[newDate.dayOfWeek.name] + " " + newDate.dayOfMonth,
            date = newDate.toString().split("-").reversed().joinToString(separator = ".")
        )
        _listDates.add(item)
    }
    listDates = _listDates
    return _listDates
}