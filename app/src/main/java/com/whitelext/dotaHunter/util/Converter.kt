package com.whitelext.dotaHunter.util

import com.example.UserListQuery
import com.example.UserProfileQuery
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

object Converter {

    fun unixToDate(value: Long?): String {
        return if (value != null) {
            val date = Date(value * 1000)
            val format = SimpleDateFormat("dd.MM.yy", Locale.ROOT)
            format.format(date)
        } else {
            ""
        }
    }

    fun steamAccountToPlayer(steamAccount: UserProfileQuery.SteamAccount): UserListQuery.Player {
        return UserListQuery.Player(
            id = steamAccount.id,
            avatar = steamAccount.avatar,
            name = steamAccount.name,
            seasonRank = steamAccount.seasonRank,
            lastMatchDateTime = steamAccount.lastMatchDateTime
        )
    }

    fun anyToLong(id: Any?): Long? {
        return id?.let { (it as BigDecimal).toLong() }
    }
}
