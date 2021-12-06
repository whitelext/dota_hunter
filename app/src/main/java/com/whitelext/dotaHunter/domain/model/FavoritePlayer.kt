package com.whitelext.dotaHunter.domain.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.UserListQuery
import com.whitelext.dotaHunter.util.Converter
import com.whitelext.dotaHunter.util.FileHelper
import com.whitelext.dotaHunter.util.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Entity(tableName = "favorite_players")
class FavoritePlayer(
    @PrimaryKey(autoGenerate = false)
    val id: Long?,
    val name: String?,
    val avatarId: String?,
    val rank: Int?,
    val lastMatchDateTime: Long?
) {

    fun getAvatarBitmap(): ImageBitmap? {
        var bitmap = FileHelper.getImageEntity(id.toString(), FileHelper.Entity.AVATAR, FileHelper.Folder.FAVORITE_PLAYERS)
        if (bitmap == null) {
            CoroutineScope(Dispatchers.IO).launch {
               bitmap = Utils.getBitmapFromURL(Utils.getAvatarUrl(avatarId))
            }
        }
        return bitmap?.asImageBitmap()
    }

    fun getRankBitmap(): ImageBitmap? {
        var bitmap = FileHelper.getImageEntity(id.toString(), FileHelper.Entity.RANK, FileHelper.Folder.RANKS)
        if (bitmap == null) {
            CoroutineScope(Dispatchers.IO).launch {
                bitmap = Utils.getBitmapFromURL(Utils.getRankUrl(rank))
            }
        }
        return bitmap?.asImageBitmap()
    }

    companion object {

        fun createFromProfile(profile: UserListQuery.Player): FavoritePlayer {
            return FavoritePlayer(
                Converter.anyToId(profile.id),
                profile.name,
                profile.avatar,
                profile.seasonRank.toString().toIntOrNull(),
                profile.lastMatchDateTime.toString().toLongOrNull()
            )
        }

    }

}

