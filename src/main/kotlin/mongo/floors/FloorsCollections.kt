package mongo.floors

import mongo.DataBase
import mongo.IdName
import mongo.getCollection

suspend fun DataBase.Floors.getAll(): List<IdName> {
    return getCollection().find().toList()
}