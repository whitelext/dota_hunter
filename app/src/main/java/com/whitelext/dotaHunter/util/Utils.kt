package com.whitelext.dotaHunter.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.MatchStatsQuery
import com.example.UserProfileQuery
import com.example.type.RankBracketHeroTimeDetail
import kotlinx.coroutines.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.KSuspendFunction1

object Utils {

    var debounceJob: Job? = null

    fun asyncCall(
        waitMs: Long = 0L,
        coroutineScope: CoroutineScope,
        destinationFunction: KSuspendFunction0<Unit>
    ): () -> Unit {
        return {
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch(
                Dispatchers.IO + CoroutineExceptionHandler {
                    _, _ ->
                }
            ) {
                delay(waitMs)
                destinationFunction()
            }
        }
    }

    fun asyncCall(
        waitMs: Long = 0L,
        coroutineScope: CoroutineScope,
        destinationFunction: KSuspendFunction1<Long, Unit>,
        argument: Long
    ): () -> Unit {
        return {
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch(
                Dispatchers.IO + CoroutineExceptionHandler {
                    _, _ ->
                }
            ) {
                delay(waitMs)
                destinationFunction(argument)
            }
        }
    }

    fun getAvatarUrl(suffix: String?) = buildString {
        append("https://cdn.akamai.steamstatic.com/steamcommunity/public/images/avatars/")
        append(suffix)
    }

    fun getHeroUrl(displayName: String?): String {
        val preparedName = displayName?.replace(" ", "_")?.lowercase()
        return "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/$preparedName.png"
    }

    fun getItemUrl(itemName: String) = buildString {
        if (itemName.contains("recipe")) {
            append("https://cdn.stratz.com/images/dota2/items/recipe.png")
        } else {
            append("https://cdn.stratz.com/images/dota2/items/")
            append("$itemName.png")
        }
    }

    fun getLockUrl() = buildString {
        append("https://cdn-icons-png.flaticon.com/512/59/59172.png")
    }

    fun getRankUrl(value: Int?) = buildString {
        if (value != null) {
            append("https://cdn.stratz.com/images/dota2/seasonal_rank/medal_${value / 10}.png")
        } else {
            append("https://cdn.stratz.com/images/dota2/seasonal_rank/medal_0.png")
        }
    }

    fun getStarsUrl(value: Int?) = buildString {
        if (value != null) {
            append("https://cdn.stratz.com/images/dota2/seasonal_rank/star_${if (value / 10 == 8) 0 else value % 10}.png")
        }
    }

    fun getLastMatchDateTime(value: Long?): String {
        return if (value != null) {
            val date = Date(value * 1000)
            val format = SimpleDateFormat("dd.MM.yy", Locale.ROOT)
            "last game: ${format.format(date)}"
        } else {
            "non active"
        }
    }

    fun redCross() = "https://cdn-icons-png.flaticon.com/64/1828/1828665.png"

    fun buybackIcon() =
        "https://static.wikia.nocookie.net/dota2_gamepedia/images/9/94/Greevil%27s_Greed_icon.png/revision/latest/scale-to-width-down/128?cb=20120801073414"

    fun aegisIcon() =
        "https://static.wikia.nocookie.net/dota2_gamepedia/images/e/ed/Aegis_of_the_Immortal_ability_icon.png/revision/latest/scale-to-width-down/128?cb=20171203200214"

    fun convertUnixToDate(value: Long?): String {
        return if (value != null) {
            val date = Date(value * 1000)
            val format = SimpleDateFormat("dd.MM.yy", Locale.ROOT)
            format.format(date)
        } else {
            ""
        }
    }

    fun getDuration(durationSeconds: Int): String {
        val minutes = durationSeconds / 60
        val seconds = durationSeconds - minutes * 60
        return "$minutes:${if (seconds < 10) "0$seconds" else seconds}"
    }

    fun getKillsDeathsAssistsProfile(match: UserProfileQuery.Player1): String {
        return "${match.kills ?: 0} / ${match.deaths ?: 0} / ${match.assists ?: 0}"
    }

    fun getKillsDeathsAssists(match: MatchStatsQuery.Player): String {
        return "${match.kills ?: 0} / ${match.deaths ?: 0} / ${match.assists ?: 0}"
    }

    fun getGpmXpm(match: MatchStatsQuery.Player): String {
        return "${match.goldPerMinute} / ${match.experiencePerMinute}"
    }

    fun getWinRate(player: UserProfileQuery.Player): String {
        return "${((player.winCount?.toFloat() ?: 0f) / (player.matchCount ?: 1) * 100).roundToInt()} %"
    }

    fun getResult(isRadiantWin: Boolean) =
        if (isRadiantWin) "Radiant Victory" else "Dire Victory"

    fun getRadiantKills(match: MatchStatsQuery.Match): Int {
//        match.players?.groupBy { it?.isRadiant == true }?.map { it.value }.sumOf {  }
        var sum = 0
        match.players?.forEach {
            if (it != null) {
                if (it.isRadiant == true) sum += it.kills?.toString()?.toInt() ?: 0
            }
        }
        return sum
    }

    fun RankBracketHeroTimeDetail.toArray(): Array<Byte> {
        return when (this) {
            RankBracketHeroTimeDetail.ALL -> arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
            RankBracketHeroTimeDetail.HERALD_GUARDIAN -> arrayOf(1, 2)
            RankBracketHeroTimeDetail.CRUSADER_ARCHON -> arrayOf(3, 4)
            RankBracketHeroTimeDetail.LEGEND_ANCIENT -> arrayOf(5, 6)
            RankBracketHeroTimeDetail.DIVINE_IMMORTAL -> arrayOf(7, 8)
            else -> arrayOf()
        }
    }

    fun getDireKills(match: MatchStatsQuery.Match): Int {
        var sum = 0
        match.players?.forEach {
            if (it != null) {
                if (it.isRadiant == false) sum += it.kills?.toString()?.toInt() ?: 0
            }
        }
        return sum
    }

    fun getBitmapFromURL(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            BitmapFactory.decodeStream(connection.inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
