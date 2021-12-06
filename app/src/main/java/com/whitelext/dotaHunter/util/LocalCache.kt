package com.whitelext.dotaHunter.util

import android.content.Context
import com.whitelext.dotaHunter.domain.model.FavoritePlayer

object LocalCache {

    fun cacheFavoritePlayer(favoritePlayer: FavoritePlayer, context: Context) {
        FileHelper.saveEntity(
            favoritePlayer.id.toString(),
            Utils.getAvatarUrl(favoritePlayer.avatarId),
            FileHelper.Entity.AVATAR,
            FileHelper.Folder.FAVORITE_PLAYERS,
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

    fun removeFavoritePlayer(favoritePlayer: FavoritePlayer) {
        // TODO: add player id to clear-list and periodically garbage-collecting cache by async service
    }

}