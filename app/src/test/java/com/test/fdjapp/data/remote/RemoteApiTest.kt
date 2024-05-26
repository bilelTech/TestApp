package com.test.fdjapp.data.remote

import com.test.fdjapp.BuildConfig
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class RemoteApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: RemoteApi

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(RemoteApi::class.java)
    }

    @Test
    fun `should return leagues list on success`() = runBlocking {
        // GIVEN
        enqueueResponse("all-leagues.json")

        // WHEN
        val leaguesResponse = service.getAllLeagues(BuildConfig.API_KEY)

        // THEN
        Assert.assertEquals(1044, leaguesResponse.leagues?.size)
        Assert.assertEquals("4328", leaguesResponse.leagues?.get(0)?.idLeague)
        Assert.assertEquals("English Premier League", leaguesResponse.leagues?.get(0)?.strLeague)
        Assert.assertEquals("Soccer", leaguesResponse.leagues?.get(0)?.strSport)
        Assert.assertEquals(
            "Premier League, EPL",
            leaguesResponse.leagues?.get(0)?.strLeagueAlternate
        )
    }

    @Test
    fun `should return team list by leagues on success`() = runBlocking {
        // GIVEN
        enqueueResponse("teams.json")

        // WHEN
        val teamsResponse = service.searchTeamsByLeague(BuildConfig.API_KEY,"French Ligue")

        // THEN
        Assert.assertEquals(18, teamsResponse.teams?.size)
        Assert.assertEquals("133704", teamsResponse.teams?.get(0)?.idTeam)
        Assert.assertEquals("119", teamsResponse.teams?.get(0)?.idSoccerXML)
        Assert.assertEquals("106", teamsResponse.teams?.get(0)?.idAPIfootball)
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("api-response/$fileName")
        val source = inputStream.source().buffer()

        val mockResponse = MockResponse()

        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }

        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }


    @After
    fun stopService() {
        mockWebServer.shutdown()
    }
}