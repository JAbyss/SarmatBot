package mongo.groups

import mongo.DataBase
import mongo.GroupDC
import mongo.getCollection
import org.litote.kmongo.and
import org.litote.kmongo.eq

suspend fun DataBase.Groups.getGroups(): List<GroupDC> {
    return getCollection().find().toList()
}

suspend fun DataBase.Groups.checkGroup(nameGroup: String): String? {
    return getCollection().findOne(GroupDC::name eq nameGroup)?.id
}

suspend fun DataBase.Groups.getGroupsByFilters(faculty: String, heading: String): List<GroupDC> {
    return getCollection().find(and(GroupDC::faculty eq faculty, GroupDC::heading eq heading)).toList()
}

suspend fun DataBase.Groups.getById(idGroup: String): GroupDC? {
    return getCollection().findOne(GroupDC::id eq idGroup)
}