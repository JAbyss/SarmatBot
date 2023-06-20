package mongo.facultys

import mongo.DataBase
import mongo.IdName
import mongo.getCollection

suspend fun DataBase.Faculty.getAll(): List<IdName> {
    return getCollection().find().toList()
}