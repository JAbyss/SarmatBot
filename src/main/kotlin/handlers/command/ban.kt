package handlers.command

import Admins
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.entities.ChatId
import mongo.DataBase
import mongo.users.banUser
import verifications.cCommand
import verifications.uId

fun Dispatcher.commandBan() = cCommand("banHammer") {
    try {
        if (Admins.contains(uId)) {
            val idUserForBan = args[0]

            if (!Admins.contains(idUserForBan)) {
                DataBase.Users.banUser(idUserForBan)
                if (args.size > 1) {
                    val message = args.drop(1).joinToString(separator = " ")
                    bot.sendMessage(
                        ChatId.fromId(idUserForBan.toLong()),
                        """
                            Вы забанены
                            
                            Причина:
                                $message
                                
                            Если вы не согласны с баном -> @JAbyss <-
                            """.trimIndent()
                    )
                } else
                    bot.sendMessage(
                        ChatId.fromId(idUserForBan.toLong()),
                        """
                                Вы забанены
                                
                                Если вы не согласны с баном -> @JAbyss <-
                            """.trimIndent()
                    )
            }
        }
    } catch (e: Exception) {
        println(e.toString())
    }
}