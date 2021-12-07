package com.whitelext.dotaHunter.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.whitelext.dotaHunter.domain.dao.FavoritePlayersDAO
import com.whitelext.dotaHunter.domain.model.FavoritePlayer

@Database(entities = [FavoritePlayer::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritePlayersDao(): FavoritePlayersDAO

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase {
            INSTANCE?.let { return INSTANCE!! }
            return synchronized(AppDatabase::class) {
                Room
                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, "myDB")
                    .build()
            }
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}
