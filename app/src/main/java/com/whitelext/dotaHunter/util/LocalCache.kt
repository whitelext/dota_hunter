package com.whitelext.dotaHunter.util

import android.content.Context
import com.whitelext.dotaHunter.domain.model.FavoritePlayer

object LocalCache {

    fun cacheFavoritePlayer(favoritePlayer: FavoritePlayer, context: Context) {
        FileHelper.saveEntity(
            favoritePlayer.id.toString(),
            Utils.getAvatarUrl(favoritePlayer.avatarId),
            FileHelper.Entity.AVATAR,
            FileHelper.Folder.FAVORITES,
            context
        )
        FileHelper.saveEntity(
            favoritePlayer.rank.toString(),
            Utils.getRankUrl(favoritePlayer.rank),
            FileHelper.Entity.RANK,
            FileHelper.Folder.RANKS,
            context
        )
    }

    fun removeFavoritePlayer(playerId: Long, context: Context) {
        FileHelper.deleteEntity(
            playerId.toString(),
            FileHelper.Entity.AVATAR,
            FileHelper.Folder.FAVORITES,
            context
        )
    }
}
