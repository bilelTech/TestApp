package com.test.fdjapp.data.remote

import com.test.fdjapp.data.remote.response.GetLeaguesResponse
import com.test.fdjapp.data.remote.response.teams.TeamsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteApi {

    @GET("{api_key}/all_leagues.php")
    suspend fun getAllLeagues(@Path("api_key") apiKey: String): GetLeaguesResponse

    @GET("{api_key}/search_all_teams.php")
    suspend fun searchTeamsByLeague(
        @Path("api_key") apiKey: String,
        @Query("l") query: String
    ): TeamsResponse
}