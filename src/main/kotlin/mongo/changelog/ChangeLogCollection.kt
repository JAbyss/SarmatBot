package mongo.changelog

import mongo.ChangeLogDc
import mongo.DataBase
import mongo.getCollection
import org.litote.kmongo.bson

suspend fun DataBase.ChangeLog.getLastChangeLog(): ChangeLogDc? {
    return try {
        getCollection().find().limit(1).sort("{\$natural: -1}".bson).toList()[0]
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}