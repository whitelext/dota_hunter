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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    @ApplicationContext val context: Context
) : FavoritesRepository {

    override suspend fun getPlayers(): Resource<List<FavoritePlayer>> {
        Log.d("QWERT", "getPlayers")
        var players = emptyList<FavoritePlayer>()
        CoroutineScope(Dispatchers.IO).launch {
            players = appDatabase.favoritePlayersDao().getPlayers()
        }
        return Resource.Success(players)
    }

    override suspend fun addPlayer(player: UserListQuery.Player): Boolean {
        return try {
            CoroutineScope(Dispatchers.IO).launch {
                val favoritePlayer = FavoritePlayer.createFromProfile(player)
                appDatabase.favoritePlayersDao().addPlayer(favoritePlayer)
                LocalCache.cacheFavoritePlayer(favoritePlayer, context)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deletePlayer(playerId: Long): Boolean {
        return try {
            CoroutineScope(Dispatchers.IO).launch {
                appDatabase.favoritePlayersDao().deletePlayer(playerId)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

}