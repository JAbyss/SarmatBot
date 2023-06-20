package mongo

import ServerDate
import kotlinx.serialization.Serializable
import mongo.DataBase.NamesCollection.audience
import mongo.DataBase.NamesCollection.buildings
import mongo.DataBase.NamesCollection.changeLogs
import mongo.DataBase.NamesCollection.facultys
import mongo.DataBase.NamesCollection.floors
import mongo.DataBase.NamesCollection.groups
import mongo.DataBase.NamesCollection.headings
import mongo.DataBase.NamesCollection.logsActivity
import mongo.DataBase.NamesCollection.schedule
import mongo.DataBase.NamesCollection.users
import org.bson.BsonDocument
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

open class MongoCollection<T>(
    val db: CoroutineDatabase,
    val name: String
)

val productionUser = "ArhaikaBot"
val testUser = "testPetApp"

val uriMongo =
    "mongodb://SarmatBot:563214789Qq@192.168.0.53:27017/?authSource=admin&replicaSet=rs1&directConnection=true"

val KClient = KMongo.createClient(uriMongo)
    .coroutine

inline fun <reified T : Any> MongoCollection<T>.getCollection(advName: String = ""): CoroutineCollection<T> {
    return db.getCollection(this.name + advName)
}

enum class ReductionFIO{
    SecondN_FN_TN, SecondN_FirstN_TH, Full
}

@Serializable
data class UsersDC(
    @BsonId
    val id: String,
    val idGroup: String = "",
    val username: String = "",
    val faculty: String = "",
    val heading: String = "",
    val lastAction: String = "",
    val mode: String = "",
    val lastMessageId: String = "",
    val reductionMessage: Boolean = false,
    val reductionFIO: ReductionFIO = ReductionFIO.Full,
    val reductionAsyncLessons: Boolean = false,
    val dateRegistration: String = ServerDate.fullDate,
    val notifyUpdate: Boolean = false,
    val isBanned: Boolean = false
)

@Serializable
data class OneLessonDc(
    val time: String,
    val name: String,
    val cab: String = "",
    val type: String = "",
    val prepod: String = ""
)

@Serializable
data class ScheduleDc(
    @BsonId
    val id: String,
    val lastUpdate: String = "",
    val listLessons: List<List<OneLessonDc>> = emptyList()
)

data class GroupDC(
    @BsonId
    val id: String,
    val name: String,
    val faculty: String = "",
    val heading: String = ""
)

data class IdName(
    @BsonId
    val id: String,
    val name: String
)

data class AudienceDC(
    @BsonId
    val id: String,
    val name: String,
    val building: String = "",
    val floor: String = ""
)

data class ActivityLogDc(
    val date: String,
    val countGotSchedules: Int
)

data class ChangeLogDc(
    @BsonId
    val version: String,
    val description: String,
    val date: String
)

object DataBase {

    val productionBot = "Sarmat"
    val testBot = "system_settings"

    val db = KClient.getDatabase(productionBot)

    object NamesCollection {
        const val users = "users"
        const val schedule = "schedule"
        const val groups = "groups"
        const val headings = "headings"
        const val facultys = "facultys"
        const val buildings = "buildings"
        const val floors = "floors"
        const val audience = "audience"
        const val logsActivity = "logsActivity"
        const val changeLogs = "changeLogs"
    }

    object Users : MongoCollection<UsersDC>(db, users)
    object Schedule : MongoCollection<ScheduleDc>(db, schedule)
    object Groups : MongoCollection<GroupDC>(db, groups)
    object Heading : MongoCollection<IdName>(db, headings)
    object Faculty : MongoCollection<IdName>(db, facultys)
    object Buildings : MongoCollection<IdName>(db, buildings)
    object Floors : MongoCollection<IdName>(db, floors)
    object Audience : MongoCollection<AudienceDC>(db, audience)
    object LogsActivity : MongoCollection<BsonDocument>(db, logsActivity)
    object ChangeLog: MongoCollection<ChangeLogDc>(db, changeLogs)
}