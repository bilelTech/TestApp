package com.test.fdjapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamData(
    @PrimaryKey
    val id: String,
    val nameLeague: String,
    val nameTeam: String,
    val image: String,
    val description: String
)