package com.test.fdjapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.fdjapp.data.models.LeagueData
import com.test.fdjapp.data.models.TeamData

@Database(version = 1, entities = [LeagueData::class, TeamData::class], exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun leagueDao(): LeagueDao
    abstract fun teamDao(): TeamDao

    companion object {
        val DATABASE_NAME: String = "app_db"
    }
}