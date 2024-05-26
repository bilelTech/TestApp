package com.test.fdjapp.domain

import com.test.fdjapp.domain.entity.LeagueEntity
import com.test.fdjapp.domain.repository.LeaguesRepository
import com.test.fdjapp.domain.usecases.GetLeaguesUseCase
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
class GetLeaguesUseCaseTest {

    /**
     * variables
     */
    @Mock
    private lateinit var leaguesRepository: LeaguesRepository
    private lateinit var getLeaguesUseCase: GetLeaguesUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getLeaguesUseCase = GetLeaguesUseCase(leaguesRepository)
    }

    @Test
    fun getLeaguesUseCaseSuccessTest() {
        runBlocking {
            val list = ArrayList<LeagueEntity>()
            list.add(LeagueEntity("123","strLeague","strleaguealt","strSport"))
            val flow = flow {
                emit(Result.success(list))
            }
            Mockito.`when`(leaguesRepository.getLeagues()).thenReturn(flow)
            val result = getLeaguesUseCase.getLeagues()
            result.collect { res: Result<List<LeagueEntity>> ->
                assert(res.isSuccess)
            }
        }
    }

    @Test
    fun getLeaguesUseCaseFailedTest() {
        runBlocking {
            val flow = flow {
                emit(Result.failure<List<LeagueEntity>>(Throwable()))
            }
            Mockito.`when`(leaguesRepository.getLeagues()).thenReturn(flow)
            val result = getLeaguesUseCase.getLeagues()
            result.collect { res: Result<List<LeagueEntity>> ->
                assert(res.isFailure)
            }
        }
    }
}