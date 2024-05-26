package com.test.fdjapp.data.remote.response

import com.test.fdjapp.data.models.LeagueData

data class GetLeaguesResponse(
    val leagues: List<LeagueData?>?
)