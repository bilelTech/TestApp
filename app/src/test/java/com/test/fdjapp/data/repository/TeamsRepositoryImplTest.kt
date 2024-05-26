package com.test.fdjapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.fdjapp.BuildConfig
import com.test.fdjapp.data.local.TeamDao
import com.test.fdjapp.data.remote.RemoteApi
import com.test.fdjapp.data.remote.response.teams.Team
import com.test.fdjapp.data.remote.response.teams.TeamsResponse
import com.test.fdjapp.domain.entity.TeamEntity
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
class TeamsRepositoryImplTest {

    /**
     * variables
     */
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var remoteApi: RemoteApi

    @Mock
    private lateinit var teamDao: TeamDao

    private lateinit var teamsRepositoryImpl: TeamsRepositoryImpl

    private var testCoroutineDispatcher = UnconfinedTestDispatcher()


    /**
     * called before start the testing method
     */
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        teamsRepositoryImpl =
            TeamsRepositoryImpl(remoteApi, teamDao, testCoroutineDispatcher)
    }

    @Test
    fun getTeamsSuccess() = runBlocking {
        val team = Team(
            idTeam = "123",
            idAPIfootball = "",
            idLeague = "",
            idLeague2 = "",
            idLeague3 = "",
            idLeague4 = "",
            idLeague5 = "",
            idLeague6 = "",
            idLeague7 = "",
            idSoccerXML = "",
            intFormedYear = "",
            intLoved = "",
            intStadiumCapacity = "",
            strAlternate = "",
            strCountry = "",
            strDescriptionCN = "",
            strDescriptionDE = "",
            strDescriptionEN = "",
            strDescriptionES = "",
            strDescriptionFR = "",
            strDescriptionHU = "",
            strDescriptionIL = "",
            strDescriptionIT = "",
            strDescriptionJP = "",
            strDescriptionNL = "",
            strDescriptionNO = "",
            strDescriptionPL = "",
            strDescriptionPT = "",
            strDescriptionRU = "",
            strDescriptionSE = "",
            strDivision = "",
            strFacebook = "",
            strGender = "",
            strInstagram = "",
            strKeywords = "",
            strKitColour1 = "",
            strKitColour2 = "",
            strKitColour3 = "",
            strLeague = "",
            strLeague2 = "",
            strLeague3 = "",
            strLeague4 = "",
            strLeague5 = "",
            strLeague6 = "",
            strLeague7 = "",
            strLocked = "",
            strRSS = "",
            strSport = "",
            strStadium = "",
            strStadiumDescription = "",
            strStadiumLocation = "",
            strStadiumThumb = "",
            strTeam = "",
            strTeamBadge = "",
            strTeamBanner = "",
            strTeamFanart1 = "",
            strTeamFanart2 = "",
            strTeamFanart3 = "",
            strTeamFanart4 = "",
            strTeamJersey = "",
            strTeamLogo = "",
            strTeamShort = "",
            strTwitter = "",
            strWebsite = "",
            strYoutube = ""
        )
        val teamsResponse = TeamsResponse(listOf(team))
        Mockito.`when`(remoteApi.searchTeamsByLeague(BuildConfig.API_KEY, "test"))
            .thenReturn(teamsResponse)
        val flow = teamsRepositoryImpl.getTeams("test")
        flow.collect { result: Result<List<TeamEntity>> ->
            assert(result.isSuccess)
        }
    }

    @Test
    fun getTeamsFailed() = runBlocking {
        Mockito.`when`(remoteApi.searchTeamsByLeague(BuildConfig.API_KEY, "test"))
            .thenThrow(RuntimeException())
        val flow = teamsRepositoryImpl.getTeams("test")
        flow.collect { result: Result<List<TeamEntity>> ->
            assert(result.isFailure)
        }
    }
}