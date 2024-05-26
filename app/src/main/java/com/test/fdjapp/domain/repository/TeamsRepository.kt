package com.test.fdjapp.domain.repository

import com.test.fdjapp.domain.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

interface TeamsRepository {
    suspend fun getTeams(league: String): Flow<Result<List<TeamEntity>>>
}