package com.whitelext.dotaHunter.domain.model

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.UserListQuery
import com.whitelext.dotaHunter.util.Converter
import com.whitelext.dotaHunter.util.FileHelper

@Entity(tableName = "favorite_players")
class FavoritePlayer(
    @PrimaryKey(autoGenerate = false)
    val id: Long?,
    val name: String?,
    val avatarId: String?,
    val rank: Int?,
    val lastMatchDateTime: Long?
) {

    fun getAvatarBitmap(context: Context): ImageBitmap? {
        val bitmap = FileHelper.getImageEntity(
            id.toString(),
            FileHelper.Entity.AVATAR,
            FileHelper.Folder.FAVORITES,
            context
        )
        return bitmap?.asImageBitmap()
    }

    fun getRankBitmap(context: Context): ImageBitmap? {
        val bitmap = FileHelper.getImageEntity(
            rank.toString(),
            FileHelper.Entity.RANK,
            FileHelper.Folder.RANKS,
            context
        )
        return bitmap?.asImageBitmap()
    }

    companion object {

        fun createFromProfile(profile: UserListQuery.Player): FavoritePlayer {
            return FavoritePlayer(
                Converter.anyToLong(profile.id),
                profile.name,
                profile.avatar,
                profile.seasonRank.toString().toIntOrNull(),
                profile.lastMatchDateTime.toString().toLongOrNull()
            )
        }
    }
}
