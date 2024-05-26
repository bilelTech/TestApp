package com.test.fdjapp.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.test.fdjapp.presentation.models.TeamView


@Composable
fun TeamsList(teamsList: List<TeamView>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        )
        {
            items(teamsList) { team ->
                TeamItem(team)
            }
        }
    }
}

/**
 * item of team
 */
@Composable
fun TeamItem(team: TeamView) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Image(
            painter = rememberAsyncImagePainter(team.image),
            contentDescription = "",
            modifier = Modifier
                .size(150.dp)
                .padding(start = 16.dp)
        )
    }
}

