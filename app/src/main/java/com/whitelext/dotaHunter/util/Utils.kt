package com.whitelext.dotaHunter.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KSuspendFunction0

object Utils {

    fun debounceCall(
        waitMs: Long = 300L,
        coroutineScope: CoroutineScope,
        destinationFunction: KSuspendFunction0<Unit>
    ): () -> Unit {
        var debounceJob: Job? = null
        return {
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch {
                delay(waitMs)
                destinationFunction()
            }
        }
    }

    fun getAvatarUrl(suffix: String?) = buildString {
        append("https://cdn.akamai.steamstatic.com/steamcommunity/public/images/avatars/")
        append(suffix)
    }

    fun getItemUrl(itemName: String) = buildString {
        append("https://cdn.stratz.com/images/dota2/items/")
        append("${itemName}.png")
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
            val format = SimpleDateFormat("dd/MM/yy", Locale.ROOT)
            "last game: ${format.format(date)}"
        } else {
            "non active"
        }
    }
}