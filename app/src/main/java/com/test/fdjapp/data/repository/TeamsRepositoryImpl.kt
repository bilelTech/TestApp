package com.test.fdjapp.data.repository

import android.content.Context
import com.test.fdjapp.BuildConfig
import com.test.fdjapp.data.constants.Constants
import com.test.fdjapp.data.local.TeamDao
import com.test.fdjapp.data.models.TeamData
import com.test.fdjapp.data.remote.RemoteApi
import com.test.fdjapp.di.IoDispatcher
import com.test.fdjapp.di.NoNetworkException
import com.test.fdjapp.domain.entity.TeamEntity
import com.test.fdjapp.domain.repository.TeamsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TeamsRepositoryImpl @Inject constructor(
    private val remoteApi: RemoteApi,
    private val teamDao: TeamDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TeamsRepository {

    /**
     * get teams by name of league
     */
    override suspend fun getTeams(league: String): Flow<Result<List<TeamEntity>>> {
        return flow {
            val teams = remoteApi.searchTeamsByLeague(BuildConfig.API_KEY, league).teams
            val list = ArrayList<TeamEntity>()
            if (teams != null && teams.isNotEmpty()) {
                teams.forEach { team ->
                    list.add(
                        TeamEntity(
                            id = team.idTeam,
                            nameTeam = team.strTeam ?: "",
                            nameLeague = team.strLeague ?: "",
                            image = team.strTeamBadge ?: "",
                            description = team.strDescriptionEN ?: ""
                        )
                    )
                    teamDao.insert(
                        TeamData(
                            id = team.idTeam,
                            nameTeam = team.strTeam ?: "",
                            nameLeague = team.strLeague ?: "",
                            image = team.strTeamBadge ?: "",
                            description = team.strDescriptionEN ?: ""
                        )
                    )
                }
            }
            val sortedList =
                list.toList().sortedBy { it.nameTeam }.reversed().slice(0 until list.size step 2)
            emit(Result.success(sortedList))
        }.catch { cause ->
            if (cause is NoNetworkException) {
                emit(
                    Result.failure(
                        Throwable(
                            Constants.NETWORK_ERROR_MSG
                        )
                    )
                )
                val list = teamDao.getTeamsByLeaguesName(league)?.map { team ->
                    TeamEntity(
                        id = team.id,
                        nameTeam = team.nameTeam ?: "",
                        nameLeague = team.nameLeague ?: "",
                        image = team.image ?: "",
                        description = team.description ?: ""
                    )
                }
                if (list?.isNotEmpty() == true) {
                    val sortedList = list.toList().sortedBy { it.nameTeam }.reversed()
                        .slice(list.indices step 2)
                    emit(Result.success(sortedList.toList()))
                }
            } else {
                emit(Result.failure(Throwable(Constants.SERVER_ERROR_MSG)))
            }
        }.flowOn(ioDispatcher)
    }
}