package handlers.command

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.entities.ParseMode
import utils.chat
import verifications.cCommand
import verifications.isNotBlocked
import verifications.uId

fun Dispatcher.commandCommands() = cCommand("commands"){
    isNotBlocked(uId){
        bot.sendMessage(
            chatId = chat(uId),
            text = """
                        <b>Команды:</b>
                        
                        /settings - Вызывает настройки, где можно изменить поведение бота под себя.
                        
                        /lastUpdate - Выводит список последних изменений.
                    """.trimIndent(),
            parseMode = ParseMode.HTML
        )
    }
}