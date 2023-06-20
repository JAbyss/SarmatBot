package mongo.buildings

import mongo.DataBase
import mongo.IdName
import mongo.getCollection

suspend fun DataBase.Buildings.getAll(): List<IdName> {
    return getCollection().find().toList()
}