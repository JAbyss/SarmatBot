package mongo.audiences

import mongo.AudienceDC
import mongo.DataBase
import mongo.GroupDC
import mongo.getCollection
import org.litote.kmongo.eq

suspend fun DataBase.Audience.getAll(building: String, floor: String): List<AudienceDC> {
    return getCollection().find(AudienceDC::building eq building, AudienceDC::floor eq floor).toList()
}

suspend fun DataBase.Audience.checkAudience(name: String): String? {
    return getCollection().findOne(AudienceDC::name eq name)?.id
}

suspend fun DataBase.Audience.getById(idAudience: String): AudienceDC? {
    return getCollection().findOne(AudienceDC::id eq idAudience)
}