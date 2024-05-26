package com.test.fdjapp.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.test.fdjapp.presentation.composables.AutoComplete
import com.test.fdjapp.presentation.theme.FDJAppTheme
import dagger.hilt.android.AndroidEntryPoint
import com.test.fdjapp.presentation.composables.TeamsList

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * variables
     */
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.getLeagues()
            val leagues = viewModel.leagues.observeAsState(initial = emptyList())
            val teams = viewModel.teams.observeAsState(initial = emptyList())
            viewModel.anError.observe(this) { errorMsg ->
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
            }
            FDJAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.White
                            )
                    ) {
                        AutoComplete(
                            leagues.value?.map { it.strLeague ?: "" }?.toList() ?: emptyList(),
                            onSelectLeague = { league ->
                                viewModel.getTeams(league)
                            })
                        TeamsList(teams.value?.toList() ?: emptyList())
                    }
                }
            }
        }
    }


}