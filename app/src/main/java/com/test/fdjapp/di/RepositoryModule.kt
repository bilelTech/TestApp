package com.test.fdjapp.di

import android.content.Context
import com.test.fdjapp.data.local.LeagueDao
import com.test.fdjapp.data.local.TeamDao
import com.test.fdjapp.data.remote.RemoteApi
import com.test.fdjapp.data.repository.LeaguesRepositoryImpl
import com.test.fdjapp.data.repository.TeamsRepositoryImpl
import com.test.fdjapp.domain.repository.LeaguesRepository
import com.test.fdjapp.domain.repository.TeamsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {


    @Singleton
    @Provides
    fun provideLeaguesRepositoryImpl(
        remoteApi: RemoteApi,
        leagueDao: LeagueDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): LeaguesRepository {
        return LeaguesRepositoryImpl(remoteApi, leagueDao, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideTeamsRepositoryImpl(
        remoteApi: RemoteApi,
        teamDao: TeamDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): TeamsRepository {
        return TeamsRepositoryImpl(remoteApi, teamDao, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideDispatcherIo(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}