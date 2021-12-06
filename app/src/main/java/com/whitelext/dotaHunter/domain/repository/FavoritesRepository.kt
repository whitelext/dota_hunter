package com.whitelext.dotaHunter.domain.repository

import com.example.UserListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.model.FavoritePlayer

interface FavoritesRepository {

    suspend fun getPlayers(callback: (Resource<List<FavoritePlayer>>) -> Unit)

    suspend fun addPlayer(player: UserListQuery.Player, callback: (Boolean) -> Unit)

    suspend fun deletePlayer(playerId: Long, callback: (Boolean) -> Unit)

}
