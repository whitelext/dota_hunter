package com.whitelext.dotaHunter.domain.repository

import android.content.Context
import android.util.Log
import com.example.UserListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.AppDatabase
import com.whitelext.dotaHunter.domain.model.FavoritePlayer
import com.whitelext.dotaHunter.util.LocalCache
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    @ApplicationContext val context: Context
) : FavoritesRepository {

    override suspend fun getPlayers(callback: (Resource<List<FavoritePlayer>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val players = appDatabase.favoritePlayersDao().getPlayers()
            withContext(Dispatchers.Main) {
                callback.invoke(Resource.Success(players))
            }
        }
    }

    override suspend fun addPlayer(player: UserListQuery.Player, callback: (Boolean) -> Unit) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val favoritePlayer = FavoritePlayer.createFromProfile(player)
                appDatabase.favoritePlayersDao().addPlayer(favoritePlayer)
                LocalCache.cacheFavoritePlayer(favoritePlayer, context)
                withContext(Dispatchers.Main) {
                    callback.invoke(true)
                }
            }
        } catch (e: Exception) {
            callback.invoke(false)
        }
    }

    override suspend fun deletePlayer(playerId: Long, callback: (Boolean) -> Unit) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                appDatabase.favoritePlayersDao().deletePlayer(playerId)
                LocalCache.removeFavoritePlayer(playerId, context)
                withContext(Dispatchers.Main) {
                    callback.invoke(true)
                }
            }
        } catch (e: Exception) {
            callback.invoke(false)
        }
    }

}