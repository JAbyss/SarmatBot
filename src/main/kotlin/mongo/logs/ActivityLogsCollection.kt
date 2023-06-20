package mongo.logs

import Admins
import ServerDate
import mongo.DataBase
import mongo.getCollection
import org.bson.BsonDocument
import org.litote.kmongo.bson

suspend fun DataBase.LogsActivity.createDay() {
    getByDay() ?: getCollection().insertOne("{ _id: '${ServerDate.date}' }".bson)
}

suspend fun DataBase.LogsActivity.getByDay(): BsonDocument? {
    return getCollection().findOne("{ _id: '${ServerDate.date}' }".bson)
}

suspend fun DataBase.LogsActivity.createHour() {
    createDay()
    val isExist = getByDay()?.get(ServerDate.hour)
    if (isExist == null)
        getCollection().findOneAndUpdate(
            "{ _id: '${ServerDate.date}' }".bson,
            "{ \$set: { '${ServerDate.hour}': { 'countGotSchedules' : 0} } }".bson
        )
}

suspend fun DataBase.LogsActivity.addGotSchedule() {
    createHour()
    getCollection().findOneAndUpdate(
        "{ _id: '${ServerDate.date}' }".bson,
        "{ \$inc: { '${ServerDate.hour}.countGotSchedules' : 1 } } ".bson
    )
}

suspend fun addGotSchedule(uId: String) {
    if (!Admins.contains(uId))
        DataBase.LogsActivity.addGotSchedule()
}

suspend fun DataBase.LogsActivity.addRegisteredUser() {
    createHour()
    getCollection().findOneAndUpdate(
        "{ _id: '${ServerDate.date}' }".bson,
        "{ \$inc: { '${ServerDate.hour}.countRegisteredUsers' : 1 } } ".bson
    )
}

suspend fun addRegisteredUser() {
    DataBase.LogsActivity.addRegisteredUser()
}