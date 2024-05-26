package com.test.fdjapp.domain.usecases

import com.test.fdjapp.domain.repository.LeaguesRepository

class GetLeaguesUseCase(private val leaguesRepository: LeaguesRepository) {
    suspend fun getLeagues() = leaguesRepository.getLeagues()
}