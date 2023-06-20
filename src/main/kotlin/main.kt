import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import mongo.IdName
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

val Admins = listOf(
    "468018135",
    "446777294"
)
var listDates = listOf<DateDC>()

val backScope = CoroutineScope(Dispatchers.Default)

var listFacs = emptyList<String>()
var listHeadings = emptyList<IdName>()
var listBuildings = emptyList<String>()
var listFloors = emptyList<String>()

const val test = "0.0.0.0"
const val test2 = "109.195.147.44"
const val prod = "sarmatServer"
const val BASE_URL = "http://$prod:32121/"
const val VERSION = "1.3.1"

//fun main() {
//    try {
//        getDaysOnWeek()
//        recursTask(ServerDate.timeToEndDay + 30.s)
//
//        embeddedServer(Netty, port = 33339, host = "0.0.0.0") {
//            configureSerialization()
//            configureRouting()
//        }.start(wait = false)
//
//        bot.startPolling()
//    } catch (e: Exception) {
//        bot.stopPolling()
//        tprint(e)
//    }
//}
//fun main() {
//
//}
internal object Main {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val url = URL("https://vk.com/mak98")
        val httpConn = url.openConnection() as HttpURLConnection
        httpConn.requestMethod = "GET"
        httpConn.setRequestProperty("authority", "vk.com")
        httpConn.setRequestProperty(
            "accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
        )
        httpConn.setRequestProperty("accept-language", "en-GB,en-US;q=0.9,en;q=0.8,ru;q=0.7")
        httpConn.setRequestProperty("cache-control", "max-age=0")
        httpConn.setRequestProperty(
            "cookie",
            "remixlang=0; remixstlid=9062589251271130955_flx8zJsXMcObcU4ylzadKQt5n9a5mJN2kySzHw0aWqc; remixstid=1815087795_syuEMSZgrgMSIkkbFj1rSzRDI25jD4zFHF5GSGSIFjw; remixflash=0.0.0; remixscreen_width=1920; remixscreen_height=1080; remixscreen_dpr=1; remixscreen_depth=24; remixscreen_orient=1; remixdt=7200; remixdark_color_scheme=0; remixcolor_scheme_mode=auto; tmr_lvid=7f409bde67e72b036a9ff02e166c8d2e; tmr_lvidTS=1663614808729; remixuas=NTJmNTExNDBhMDQ2ZGQwYmEwZThjYzZl; remixluas2=YzNkMTFmNGFhYmU4ZmZlNmEzN2FjZjhj; remixdmgr=fe4648e6bfdddf1af7b5d87f2f26ddc1aae3043b1a1cd1eb869c7ddeff9ea0ed; remixvideo_menu_collapsed=1; remixmdevice=1920/1080/1/!!-!!!!!!!!; remixstickers_hash=28d093576c9823cf8e0f5bcfc91ddfc1; remixua=157%7C-1%7C194%7C4199637412; remixvkcom=1; remixgp=08474360333d795a7db844975ffe02c4; remixpuad=jJxmYIHOb8yeZIMU97eK_d974LqiiCcNh3O5pQNltfc; remixnsid=vk1.a.ZLd2TDgjM8SNVrMDvfMo074d1MigMf6d8pBEkm1idY_SW6yMG1zf4_7YPe2wNCQMnTVfKH92Nj_7oEW3W0DBYOJjG8lBQ64_kukdV7vbW3LY5NnlnAyfqPonKLSxFENqLKHHrx73u2RDhkIE9WOYWkgU3cTbAh0s6zagl0z6u5wBDEABo2oQBNkMwqYqo0EY; remixsid=1_NxIZDcKAqIRcvkTr-1bzwaoaIWLRYwaPNW8c45lK3bcmZVtWx6AKUhWUDTRq9wT9PSJvMvCw69TSEvb495Uyng; remixrefkey=58cfcf6bb7ad42fb4b; remixscreen_winzoom=1.95; tmr_detect=1%7C1666958524675; tmr_reqNum=1203; remixcurr_audio=-2001595924_96595924"
        )
        httpConn.setRequestProperty(
            "sec-ch-ua",
            "\"Chromium\";v=\"106\", \"Google Chrome\";v=\"106\", \"Not;A=Brand\";v=\"99\""
        )
        httpConn.setRequestProperty("sec-ch-ua-mobile", "?0")
        httpConn.setRequestProperty("sec-ch-ua-platform", "\"Linux\"")
        httpConn.setRequestProperty("sec-fetch-dest", "document")
        httpConn.setRequestProperty("sec-fetch-mode", "navigate")
        httpConn.setRequestProperty("sec-fetch-site", "same-origin")
        httpConn.setRequestProperty("sec-fetch-user", "?1")
        httpConn.setRequestProperty("upgrade-insecure-requests", "1")
        httpConn.setRequestProperty(
            "user-agent",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
        )
        val responseStream = if (httpConn.responseCode / 100 == 2) httpConn.inputStream else httpConn.errorStream
        val s = Scanner(responseStream).useDelimiter("\\A")
        val response = if (s.hasNext()) s.next() else ""
        println(response)
    }
}
//internal object Main {
//    @Throws(IOException::class)
//    @JvmStatic
//    fun main(args: Array<String>) {
//        val url = URL("https://vk.com/kalterfad")
//        val httpConn = url.openConnection() as HttpURLConnection
//        httpConn.requestMethod = "GET"
//        httpConn.setRequestProperty("authority", "vk.com")
//        httpConn.setRequestProperty(
//            "accept",
//            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
//        )
//        httpConn.setRequestProperty("accept-language", "en-GB,en-US;q=0.9,en;q=0.8,ru;q=0.7")
//        httpConn.setRequestProperty("cache-control", "max-age=0")
//        httpConn.setRequestProperty(
//            "cookie",
//            "remixlang=0; remixstlid=9062589251271130955_flx8zJsXMcObcU4ylzadKQt5n9a5mJN2kySzHw0aWqc; remixstid=1815087795_syuEMSZgrgMSIkkbFj1rSzRDI25jD4zFHF5GSGSIFjw; remixflash=0.0.0; remixscreen_width=1920; remixscreen_height=1080; remixscreen_dpr=1; remixscreen_depth=24; remixscreen_orient=1; remixdt=7200; remixdark_color_scheme=0; remixcolor_scheme_mode=auto; tmr_lvid=7f409bde67e72b036a9ff02e166c8d2e; tmr_lvidTS=1663614808729; remixuas=NTJmNTExNDBhMDQ2ZGQwYmEwZThjYzZl; remixluas2=YzNkMTFmNGFhYmU4ZmZlNmEzN2FjZjhj; remixdmgr=fe4648e6bfdddf1af7b5d87f2f26ddc1aae3043b1a1cd1eb869c7ddeff9ea0ed; remixvideo_menu_collapsed=1; remixmdevice=1920/1080/1/!!-!!!!!!!!; remixstickers_hash=28d093576c9823cf8e0f5bcfc91ddfc1; remixua=157%7C-1%7C194%7C4199637412; remixvkcom=1; remixgp=08474360333d795a7db844975ffe02c4; remixpuad=jJxmYIHOb8yeZIMU97eK_d974LqiiCcNh3O5pQNltfc; remixnsid=vk1.a.ZLd2TDgjM8SNVrMDvfMo074d1MigMf6d8pBEkm1idY_SW6yMG1zf4_7YPe2wNCQMnTVfKH92Nj_7oEW3W0DBYOJjG8lBQ64_kukdV7vbW3LY5NnlnAyfqPonKLSxFENqLKHHrx73u2RDhkIE9WOYWkgU3cTbAh0s6zagl0z6u5wBDEABo2oQBNkMwqYqo0EY; remixsid=1_NxIZDcKAqIRcvkTr-1bzwaoaIWLRYwaPNW8c45lK3bcmZVtWx6AKUhWUDTRq9wT9PSJvMvCw69TSEvb495Uyng; remixrefkey=58cfcf6bb7ad42fb4b; remixscreen_winzoom=1; remixcurr_audio=-2001806033_50806033; tmr_detect=1%7C1666958256136; tmr_reqNum=1198"
//        )
//        httpConn.setRequestProperty(
//            "sec-ch-ua",
//            "\"Chromium\";v=\"106\", \"Google Chrome\";v=\"106\", \"Not;A=Brand\";v=\"99\""
//        )
//        httpConn.setRequestProperty("sec-ch-ua-mobile", "?0")
//        httpConn.setRequestProperty("sec-ch-ua-platform", "\"Linux\"")
//        httpConn.setRequestProperty("sec-fetch-dest", "document")
//        httpConn.setRequestProperty("sec-fetch-mode", "navigate")
//        httpConn.setRequestProperty("sec-fetch-site", "same-origin")
//        httpConn.setRequestProperty("sec-fetch-user", "?1")
//        httpConn.setRequestProperty("upgrade-insecure-requests", "1")
//        httpConn.setRequestProperty(
//            "user-agent",
//            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
//        )
//        val responseStream = if (httpConn.responseCode / 100 == 2) httpConn.inputStream else httpConn.errorStream
//        val s = Scanner(responseStream).useDelimiter("\\A")
//        val response = if (s.hasNext()) s.next() else ""
//        println(response)
//    }
//}