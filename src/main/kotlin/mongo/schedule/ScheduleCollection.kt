package mongo.schedule

import mongo.DataBase
import mongo.ScheduleDc
import mongo.getCollection
import org.litote.kmongo.eq

suspend fun DataBase.Schedule.getByDateAndIdGroup(date: String, idGroup: String): ScheduleDc? {
    val id = "$date $idGroup"
    return getCollection().findOne(ScheduleDc::id eq id)
}