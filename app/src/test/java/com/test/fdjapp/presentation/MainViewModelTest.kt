package com.test.fdjapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.fdjapp.data.constants.Constants
import com.test.fdjapp.domain.entity.LeagueEntity
import com.test.fdjapp.domain.entity.TeamEntity
import com.test.fdjapp.domain.usecases.GetLeaguesUseCase
import com.test.fdjapp.domain.usecases.GetTeamsUseCase
import com.test.fdjapp.presentation.ui.MainViewModel
import com.test.fdjapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainViewModelTest {

    /**
     * variables
     */
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var mainViewModel: MainViewModel

    @Mock
    private lateinit var getLeaguesUseCase: GetLeaguesUseCase

    @Mock
    private lateinit var getTeamsUseCase: GetTeamsUseCase

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        mainViewModel = MainViewModel(getLeaguesUseCase, getTeamsUseCase)
    }

    @Test
    fun getLeaguesSuccess() {
        runBlocking {
            val list = ArrayList<LeagueEntity>()
            val leagueEntity = LeagueEntity("123", "strLeague", "strleaguealt", "strSport")
            list.add(leagueEntity)
            val flow = flow {
                emit(Result.success(list))
            }
            Mockito.`when`(getLeaguesUseCase.getLeagues()).thenReturn(flow)
            mainViewModel.getLeagues()
            val result = mainViewModel.leagues.getOrAwaitValue()
            assert(result?.size == list.size)
            assert(result?.get(0)?.idLeague == list[0].idLeague)
            assert(result?.get(0)?.strLeague == list[0].strLeague)
            assert(result?.get(0)?.strLeagueAlternate == list[0].strLeagueAlternate)
            assert(result?.get(0)?.strSport == list[0].strSport)
        }
    }

    @Test
    fun getLeaguesFailed() {
        runBlocking {
            val flow = flow {
                emit(Result.failure<List<LeagueEntity>>(RuntimeException(Constants.SERVER_ERROR_MSG)))
            }
            Mockito.`when`(getLeaguesUseCase.getLeagues()).thenReturn(flow)
            mainViewModel.getLeagues()
            val result = mainViewModel.anError.getOrAwaitValue()
            assertEquals(result, Constants.SERVER_ERROR_MSG)
        }
    }



    @Test
    fun getTeamsByLeagueSuccess() {
        runBlocking {
            val list = ArrayList<TeamEntity>()
            list.add(TeamEntity("123","nameLeague","nameTeam","image","description"))
            val flow = flow {
                emit(Result.success(list))
            }
            Mockito.`when`(getTeamsUseCase.getTeams("test")).thenReturn(flow)
            mainViewModel.getTeams("test")
            val result = mainViewModel.teams.getOrAwaitValue()
            assert(result?.size == list.size)
            assert(result?.get(0)?.id == list[0].id)
            assert(result?.get(0)?.description == list[0].description)
            assert(result?.get(0)?.nameLeague == list[0].nameLeague)
            assert(result?.get(0)?.nameTeam == list[0].nameTeam)
            assert(result?.get(0)?.image == list[0].image)
        }
    }

    @Test
    fun getTeamsByLeagueFailed() {
        runBlocking {
            val flow = flow {
                emit(Result.failure<List<TeamEntity>>(RuntimeException(Constants.SERVER_ERROR_MSG)))
            }
            Mockito.`when`(getTeamsUseCase.getTeams("test")).thenReturn(flow)
            mainViewModel.getTeams("test")
            val result = mainViewModel.anError.getOrAwaitValue()
            assertEquals(result, Constants.SERVER_ERROR_MSG)
        }
    }
}