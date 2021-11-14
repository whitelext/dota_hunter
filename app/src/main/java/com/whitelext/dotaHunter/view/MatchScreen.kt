package com.whitelext.dotaHunter.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.MatchStatsQuery
import com.whitelext.dotaHunter.MatchViewModel
import com.whitelext.dotaHunter.ui.theme.BackgroundDark


@Composable
fun MatchScreen(
    matchViewModel: MatchViewModel = hiltViewModel(),
    matchId: Long
) {
    val match by matchViewModel.matchData.observeAsState()
    matchViewModel.initMatch(matchId)
//
    Column(
        Modifier
            .background(BackgroundDark)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        match?.let { match ->
            // TODO: implement onAddToFavoritesClickListener
//            MatchCard()
            match.players?.let { Players(players = match.players.filterNotNull()) }
        }
    }
}

@Composable
fun Players(players: List<MatchStatsQuery.Player>) {

}
