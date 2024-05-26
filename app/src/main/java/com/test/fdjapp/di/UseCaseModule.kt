package com.test.fdjapp.di

import com.test.fdjapp.domain.repository.LeaguesRepository
import com.test.fdjapp.domain.repository.TeamsRepository
import com.test.fdjapp.domain.usecases.GetLeaguesUseCase
import com.test.fdjapp.domain.usecases.GetTeamsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {

    @Singleton
    @Provides
    fun provideGetLeaguesUseCase(leaguesRepository: LeaguesRepository): GetLeaguesUseCase {
        return GetLeaguesUseCase(leaguesRepository)
    }

    @Singleton
    @Provides
    fun provideGetTeamsUseCase(teamsRepository: TeamsRepository): GetTeamsUseCase {
        return GetTeamsUseCase(teamsRepository)
    }
}