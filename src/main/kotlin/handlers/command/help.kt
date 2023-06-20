package handlers.command

import VERSION
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.entities.ParseMode
import utils.chat
import verifications.cCommand
import verifications.isNotBlocked
import verifications.uId

fun Dispatcher.commandHelp() = cCommand("help"){
        isNotBlocked(uId){
            bot.sendMessage(
                chatId = chat(uId),
                text = """
                        Для начала работы бота необходимо выбрать факультет либо аудиторию.
                        
                        Для того что бы узнать расписание, не входящее в текущую неделю, напишите нужную дату по данному шаблону: 30.09.2022
                        
                        ┌Если вы не нашли вашу группу попробуйте написать ее название (так же как на сайте)      
                        ├Если бот работает не исправно, то нужно перезапустить его (ввести команду /start).
                        ├Если расписание приходит не корректно, пишите <a href="t.me/JAbyss">мне</a> и присылайте скрин ли пересылайте собщение.
                        └Если бот умер, есть какие-то пожелания или вопросы, напишите <a href="t.me/JAbyss">мне</a>.
                        
                        Бот будет активно развиваться для упрощения учебы, так что не расходитесь  🛋
                        
                        Посмотреть список всех доступных комманд: /commands
                        Спасибо Кириллу Левченко за аватарку ✌
                        Мне - @JAbyss
                        Version - $VERSION
                    """.trimIndent(),
                parseMode = ParseMode.HTML
            )
        }
}