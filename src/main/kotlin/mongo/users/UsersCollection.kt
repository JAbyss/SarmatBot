package mongo.users

import mongo.DataBase
import mongo.ReductionFIO
import mongo.UsersDC
import mongo.getCollection
import mongo.logs.addRegisteredUser
import org.litote.kmongo.eq
import org.litote.kmongo.set
import org.litote.kmongo.setTo
import org.litote.kmongo.setValue
import java.lang.Exception

suspend fun DataBase.Users.getUserById(idUser: String): UsersDC? {
    return getCollection().findOne(UsersDC::id eq idUser)
}

suspend fun DataBase.Users.insertUser(idUser: String, username: String = "") {
    try {
        getCollection().insertOne(UsersDC(id = idUser, username = username))
        addRegisteredUser()
    } catch (_: Exception) {
    }
}

suspend fun DataBase.Users.setGroup(idUser: String, idGroup: String) {
    getCollection().updateOne(UsersDC::id eq idUser, setValue(UsersDC::idGroup, idGroup))
}

suspend fun DataBase.Users.setFaculty(idUser: String, faculty: String) {
    getCollection().updateOne(UsersDC::id eq idUser, setValue(UsersDC::faculty, faculty))
}

suspend fun DataBase.Users.setHeading(idUser: String, heading: String) {
    getCollection().updateOne(UsersDC::id eq idUser, setValue(UsersDC::heading, heading))
}

suspend fun DataBase.Users.setLastAction(uId: String, action: String) {
    getCollection().updateOne(UsersDC::id eq uId, setValue(UsersDC::lastAction, action))
}

suspend fun DataBase.Users.getLastAction(uId: String): String {
    return getCollection().findOne(UsersDC::id eq uId)?.lastAction!!
}

suspend fun DataBase.Users.setMode(uId: String, mode: String) {
    getCollection().updateOne(UsersDC::id eq uId, setValue(UsersDC::mode, mode))
}

suspend fun DataBase.Users.setLastMessageId(uId: String, mId: String) {
    getCollection().updateOne(UsersDC::id eq uId, setValue(UsersDC::lastMessageId, mId))
}

suspend fun DataBase.Users.getLastMessageId(uId: String): String? {
    return getCollection().findOne(UsersDC::id eq uId)?.lastMessageId
}

suspend fun DataBase.Users.getAll(): List<UsersDC> {
    return getCollection().find().toList()
}

suspend fun DataBase.Users.banUser(uId: String) {
    getCollection().updateOne(UsersDC::id eq uId, setValue(UsersDC::isBanned, true))
}

suspend fun DataBase.Users.unbanUser(uId: String) {
    getCollection().updateOne(UsersDC::id eq uId, setValue(UsersDC::isBanned, false))
}

suspend fun DataBase.Users.deleteUser(uId: String) {
    getCollection().deleteOne(UsersDC::id eq uId)
}

suspend fun DataBase.Users.setReductionLesson(uId: String, value: Boolean) {
    getCollection().updateOne(UsersDC::id eq uId, setValue(UsersDC::reductionMessage, value))
}

suspend fun DataBase.Users.setReductionFIO(uId: String, value: ReductionFIO) {
    getCollection().updateOne(UsersDC::id eq uId, setValue(UsersDC::reductionFIO, value))
}

suspend fun DataBase.Users.setReductionAsyncLessons(uId: String, value: Boolean) {
    getCollection().updateOne(UsersDC::id eq uId, setValue(UsersDC::reductionAsyncLessons, value))
}

suspend fun DataBase.Users.setNotifyUpdateSchedule(uId: String, value: Boolean) {
    getCollection().updateOne(UsersDC::id eq uId, setValue(UsersDC::notifyUpdate, value))
}

suspend fun DataBase.Users.clearInfoUser(uId: String) {
    getCollection().updateOne(
        UsersDC::id eq uId,
        set(
            UsersDC::faculty setTo "",
            UsersDC::heading setTo "",
            UsersDC::idGroup setTo "",
            UsersDC::mode setTo "",
            UsersDC::lastMessageId setTo "",
            UsersDC::lastAction setTo ""
        )
    )
}