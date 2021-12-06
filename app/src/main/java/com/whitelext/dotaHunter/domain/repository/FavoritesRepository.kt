package com.whitelext.dotaHunter.domain.repository

import com.example.UserListQuery
import com.whitelext.dotaHunter.common.Resource
import com.whitelext.dotaHunter.domain.model.FavoritePlayer

interface FavoritesRepository {

    suspend fun getPlayers(): Resource<List<FavoritePlayer>>

    suspend fun addPlayer(player: UserListQuery.Player): Boolean

    suspend fun deletePlayer(playerId: Long): Boolean

}
