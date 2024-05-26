package com.test.fdjapp.domain

import com.test.fdjapp.domain.entity.LeagueEntity
import com.test.fdjapp.domain.entity.TeamEntity
import com.test.fdjapp.domain.repository.TeamsRepository
import com.test.fdjapp.domain.usecases.GetTeamsUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class GetTeamsUseCaseTest {

    /**
     * variables
     */
    @Mock
    private lateinit var teamsRepository: TeamsRepository
    private lateinit var getTeamsUseCase: GetTeamsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getTeamsUseCase = GetTeamsUseCase(teamsRepository)
    }

    @Test
    fun getTeamsUseCaseSuccess() {
        runBlocking {
            val list = ArrayList<TeamEntity>()
            list.add(TeamEntity("123","nameLeague","nameTeam","image","description"))
            val flow = flow {
                emit(Result.success(list))
            }
            Mockito.`when`(teamsRepository.getTeams("test")).thenReturn(flow)
            val result = getTeamsUseCase.getTeams("test")
            result.collect { result: Result<List<TeamEntity>> ->
                assert(result.isSuccess)
            }
        }
    }

    @Test
    fun getTeamsUseCaseFailed() {
        runBlocking {
            val flow = flow {
                emit(Result.failure<List<TeamEntity>>(Throwable()))
            }
            Mockito.`when`(teamsRepository.getTeams("test")).thenReturn(flow)
            val result = getTeamsUseCase.getTeams("test")
            result.collect { resultat: Result<List<TeamEntity>> ->
                assert(resultat.isFailure)
            }
        }
    }
}