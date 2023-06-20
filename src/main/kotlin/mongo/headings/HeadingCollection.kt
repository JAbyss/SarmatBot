package mongo.headings

import mongo.DataBase
import mongo.IdName
import mongo.getCollection

suspend fun DataBase.Heading.getAll(): List<IdName> {
    return getCollection().find().toList()
}