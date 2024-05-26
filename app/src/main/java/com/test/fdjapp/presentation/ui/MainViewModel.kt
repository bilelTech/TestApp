package com.test.fdjapp.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.fdjapp.domain.usecases.GetLeaguesUseCase
import com.test.fdjapp.domain.usecases.GetTeamsUseCase
import com.test.fdjapp.presentation.models.LeaguesView
import com.test.fdjapp.presentation.models.TeamView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLeaguesUseCase: GetLeaguesUseCase,
    private val getTeamsUseCase: GetTeamsUseCase
) : ViewModel() {

    /**
     * variables
     */
    private val TAG = "MainViewModel"
    private val _leagues = MutableLiveData<List<LeaguesView>?>()
    val leagues: LiveData<List<LeaguesView>?> = _leagues
    private val _teams = MutableLiveData<List<TeamView>?>()
    val teams: LiveData<List<TeamView>?> = _teams
    private val _anError = MutableLiveData<String>()
    val anError: LiveData<String> = _anError

    /**
     * get Leagues list
     */
    fun getLeagues() {
        viewModelScope.launch {
            getLeaguesUseCase.getLeagues().catch {
                Timber.e(TAG, "getLeagues onFailed")
                _anError.value = it.message
            }.collect { res ->
                res.onFailure {
                    Timber.e(TAG, "getLeagues onFailed")
                    _anError.value = it.message
                }
                res.onSuccess { leaguesList ->
                    Timber.d(TAG, "getLeagues onSuccess")
                    _leagues.value = leaguesList.map {
                        LeaguesView(
                            idLeague = it.idLeague,
                            strLeague = it.strLeague,
                            strLeagueAlternate = it.strLeagueAlternate,
                            strSport = it.strSport
                        )
                    }
                }
            }
        }
    }

    /**
     * get Teams by name league
     */
    fun getTeams(League: String) {
        viewModelScope.launch {
            getTeamsUseCase.getTeams(League).catch {
                Timber.e(TAG, "getTeams onFailed")
                _anError.value = it.message
            }.collect { res ->
                res.onFailure {
                    Timber.e(TAG, "getTeams onFailed")
                    _anError.value = it.message
                }
                res.onSuccess { teamsList ->
                    Timber.d(TAG, "getTeams onSuccess")
                    _teams.value = teamsList.map {
                        TeamView(
                            id = it.id,
                            nameLeague = it.nameLeague,
                            nameTeam = it.nameTeam,
                            image = it.image,
                            description = it.description
                        )
                    }
                }
            }
        }
    }
}