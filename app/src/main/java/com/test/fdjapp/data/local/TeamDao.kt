package com.test.fdjapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.fdjapp.data.models.TeamData

@Dao
interface TeamDao {

    // insert LeagueData on the table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: TeamData)

    // get list of teams by nameLeague
    @Query("SELECT * FROM teams WHERE nameLeague=:name")
    fun getTeamsByLeaguesName(name: String): List<TeamData>?

    // get list of teams from table
    @Query("SELECT * FROM teams")
    fun getTeams(): List<TeamData>

    //delete all leagues from table
    @Query("DELETE FROM teams")
    fun clear()
}