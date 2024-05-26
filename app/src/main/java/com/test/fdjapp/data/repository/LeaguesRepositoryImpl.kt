package com.test.fdjapp.data.repository

import android.content.Context
import com.test.fdjapp.BuildConfig
import com.test.fdjapp.R
import com.test.fdjapp.data.constants.Constants
import com.test.fdjapp.data.local.LeagueDao
import com.test.fdjapp.data.models.LeagueData
import com.test.fdjapp.data.remote.RemoteApi
import com.test.fdjapp.di.IoDispatcher
import com.test.fdjapp.di.NoNetworkException
import com.test.fdjapp.domain.entity.LeagueEntity
import com.test.fdjapp.domain.repository.LeaguesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LeaguesRepositoryImpl @Inject constructor(
    private val remoteApi: RemoteApi,
    private val leagueDao: LeagueDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LeaguesRepository {

    /**
     * get list of Leagues From api
     */
    override suspend fun getLeagues(): Flow<Result<List<LeagueEntity>>> {
        return flow {
            val leagues = remoteApi.getAllLeagues(BuildConfig.API_KEY).leagues
            val list = ArrayList<LeagueEntity>()
            leagueDao.clear()
            leagues?.forEach { league ->
                list.add(
                    LeagueEntity(
                        idLeague = league?.idLeague ?: "",
                        strLeague = league?.strLeague ?: "",
                        strLeagueAlternate = league?.strLeagueAlternate ?: "",
                        strSport = league?.strSport ?: ""
                    )
                )
                leagueDao.insert(
                    LeagueData(
                        league?.idLeague ?: "",
                        league?.strLeague ?: "",
                        league?.strLeagueAlternate ?: "",
                        league?.strSport ?: ""
                    )
                )
            }
            emit(Result.success(list.toList()))
        }.catch { cause ->
            if (cause is NoNetworkException) {
                emit(
                    Result.failure(
                        Throwable(
                            Constants.NETWORK_ERROR_MSG
                        )
                    )
                )
                val list = leagueDao.getLeagues()?.map { league ->
                    LeagueEntity(
                        idLeague = league.idLeague,
                        strLeague = league.strLeague,
                        strLeagueAlternate = league.strLeagueAlternate,
                        strSport = league.strSport
                    )
                }
                if (list?.isNotEmpty() == true) {
                    emit(Result.success(list))
                }
            } else {
                emit(Result.failure(Throwable(Constants.SERVER_ERROR_MSG)))
            }
        }.flowOn(ioDispatcher)
    }
}