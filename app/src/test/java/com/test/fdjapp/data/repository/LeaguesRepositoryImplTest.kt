package com.test.fdjapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.test.fdjapp.BuildConfig
import com.test.fdjapp.data.constants.Constants
import com.test.fdjapp.data.local.LeagueDao
import com.test.fdjapp.data.models.LeagueData
import com.test.fdjapp.data.remote.RemoteApi
import com.test.fdjapp.data.remote.response.GetLeaguesResponse
import com.test.fdjapp.domain.entity.LeagueEntity
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class LeaguesRepositoryImplTest {

    /**
     * variables
     */
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var remoteApi: RemoteApi

    @Mock
    private var leaguesDao: LeagueDao = mock()

    private lateinit var leaguesRepositoryImpl: LeaguesRepositoryImpl

    private var testCoroutineDispatcher = UnconfinedTestDispatcher()

    /**
     * called before start the testing method
     */
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        leaguesRepositoryImpl =
            LeaguesRepositoryImpl(remoteApi, leaguesDao, testCoroutineDispatcher)
    }

    @Test
    fun getLeaguesSuccess() = runBlocking {
        val list = ArrayList<LeagueEntity>()
        val LeagueDatas = ArrayList<LeagueData>()
        val leagueData = LeagueData("123","strLeague","strleaguealt","strSport")
        LeagueDatas.add(leagueData)
        list.add(LeagueEntity("123","strLeague","strleaguealt","strSport"))
        Mockito.`when`(remoteApi.getAllLeagues(BuildConfig.API_KEY)).thenReturn(GetLeaguesResponse(LeagueDatas))
        val flow = leaguesRepositoryImpl.getLeagues()

        flow.collect { result: Result<List<LeagueEntity>> ->
            assert(result.isSuccess)
        }
        verify(remoteApi).getAllLeagues(BuildConfig.API_KEY)
        verify(leaguesDao).clear()
        verify(leaguesDao).insert(leagueData)
    }

    @Test
    fun getLeaguesFailed() = runBlocking {
        Mockito.`when`(remoteApi.getAllLeagues(BuildConfig.API_KEY)).thenThrow(RuntimeException())
        val flow = leaguesRepositoryImpl.getLeagues()
        flow.collect { result: Result<List<LeagueEntity>> ->
            assert(result.isFailure)
            result.onFailure {
                assert(it.message == Constants.SERVER_ERROR_MSG)
            }
        }
    }
}