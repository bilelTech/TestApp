package com.test.fdjapp.domain.usecases

import com.test.fdjapp.domain.repository.TeamsRepository

class GetTeamsUseCase(private val teamsRepository: TeamsRepository) {
    suspend fun getTeams(league: String) = teamsRepository.getTeams(league)
}