package com.test.fdjapp.domain.repository

import com.test.fdjapp.domain.entity.LeagueEntity
import kotlinx.coroutines.flow.Flow

interface LeaguesRepository {
    suspend fun getLeagues(): Flow<Result<List<LeagueEntity>>>
}