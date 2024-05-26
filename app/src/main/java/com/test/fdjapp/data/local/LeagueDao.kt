package com.test.fdjapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.fdjapp.data.models.LeagueData

@Dao
interface LeagueDao {

    // insert LeagueData on the table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: LeagueData)

    // get list of LeagueData from table
    @Query("SELECT * FROM leagues")
    fun getLeagues(): List<LeagueData>?

    //delete all leagues from table
    @Query("DELETE FROM leagues")
    fun clear()
}