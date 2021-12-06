package com.whitelext.dotaHunter.domain.dao

import androidx.room.*
import com.whitelext.dotaHunter.domain.model.FavoritePlayer

@Dao
interface FavoritePlayersDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlayer(favoritePlayer: FavoritePlayer)

    @Query("DELETE FROM favorite_players WHERE id = :playerId")
    fun deletePlayer(playerId: Long)

    @Query("SELECT * FROM favorite_players")
    fun getPlayers(): List<FavoritePlayer>

}