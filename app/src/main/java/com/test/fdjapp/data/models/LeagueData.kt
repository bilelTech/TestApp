package com.test.fdjapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leagues")
data class LeagueData(
    @PrimaryKey
    val idLeague: String,
    val strLeague: String?,
    val strLeagueAlternate: String?,
    val strSport: String?
)