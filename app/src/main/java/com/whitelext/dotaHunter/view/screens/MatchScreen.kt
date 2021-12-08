package com.whitelext.dotaHunter.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.MatchStatsQuery
import com.whitelext.dotaHunter.ui.theme.*
import com.whitelext.dotaHunter.util.Constants
import com.whitelext.dotaHunter.util.Constants.GPM_XPM
import com.whitelext.dotaHunter.util.Constants.HEAL
import com.whitelext.dotaHunter.util.Constants.KILLS_DEATH_ASSISTS
import com.whitelext.dotaHunter.util.Constants.MONEY_PICTURE
import com.whitelext.dotaHunter.util.Constants.SWORD
import com.whitelext.dotaHunter.util.Constants.TOWER
import com.whitelext.dotaHunter.util.Screen
import com.whitelext.dotaHunter.util.Utils
import com.whitelext.dotaHunter.util.Utils.getDireKills
import com.whitelext.dotaHunter.util.Utils.getDuration
import com.whitelext.dotaHunter.util.Utils.getGpmXpm
import com.whitelext.dotaHunter.util.Utils.getKillsDeathsAssists
import com.whitelext.dotaHunter.util.Utils.getRadiantKills
import com.whitelext.dotaHunter.util.Utils.getResult
import com.whitelext.dotaHunter.view.CommonComponents.BackpackItemGrid
import com.whitelext.dotaHunter.view.CommonComponents.ItemsGrid
import com.whitelext.dotaHunter.view.CommonComponents.TextLabelRounded
import com.whitelext.dotaHunter.view.CommonComponents.TextLabelWithPictureRounded
import com.whitelext.dotaHunter.view.CommonComponents.TextLabelWithTwoRows
import com.whitelext.dotaHunter.viewModels.MatchViewModel
import java.math.BigDecimal

@Composable
fun MatchScreen(
    matchViewModel: MatchViewModel = hiltViewModel(),
    matchId: Long,
    navController: NavController
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
            MatchCard(match)
            match.players?.let {
                Players(
                    players = match.players.filterNotNull(),
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun MatchCard(
    match: MatchStatsQuery.Match,
) {
    Column(
        modifier = Modifier
            .padding(7.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = PlayerField)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = getResult(match.didRadiantWin == true),
                color = if (match.didRadiantWin == true) Win else Lose,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 40.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                color = Win,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 40.sp,
                text = getRadiantKills(match).toString(),
                modifier = Modifier.weight(1f)
            )
            match.durationSeconds?.let {
                getDuration(it)
            }?.let {
                Text(
                    color = BackgroundDark,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily,
                    fontSize = 35.sp,
                    text = it,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                color = Lose,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFamily,
                fontSize = 40.sp,
                text = getDireKills(match).toString(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun Players(
    players: List<MatchStatsQuery.Player>,
    navController: NavController
) {
    LazyColumn {
        items(players.size) { i ->
            ShowPlayer(onClick = {
                println((players[i].steamAccount?.id as BigDecimal).toLong())
                navController.navigate(
                    Screen.ProfileDetail.createRoute(
                        (players[i].steamAccount?.id as BigDecimal).toLong()
                    )
                )
            }, player = players[i])
            }
        }
    }

    @Composable
    private fun ShowPlayer(
        player: MatchStatsQuery.Player,
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .background(
                    color = if (player.steamAccount == null) DeletedAccount else if (player.isRadiant == true) Radiant else Dire
                )
                .clickable { if (player.steamAccount != null) onClick() }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .padding(horizontal = 5.dp)
            ) {
                val painter = rememberImagePainter(
                    data = Utils.getHeroUrl(player.hero?.shortName),
                    builder = {
                        crossfade(500)
                    }
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding()
                        .padding(end = 2.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .weight(1f)
                        .fillMaxWidth(fraction = Constants.THIRTYTHIRD)
                        .height(94.dp)
                        .align(Alignment.CenterVertically)
                )

                ItemsGrid(
                    items = listOf(
                        player.item0Id,
                        player.item1Id,
                        player.item2Id,
                        player.item3Id,
                        player.item4Id,
                        player.item5Id
                    )
                        // .map { itemId -> if (itemId == null) -1 else (itemId as BigDecimal).toShort() })
                        .map { itemId -> (itemId as BigDecimal?)?.toShort() ?: -1 },
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .padding(vertical = 3.dp)
            ) {
                Text(
                    text = player.steamAccount?.name ?: "Deleted account",
                    modifier = Modifier
                        .fillMaxWidth(fraction = Constants.HALF)
                        .weight(1f)
                        .align(Alignment.CenterVertically),

                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = poppinsFamily,
                    fontSize = 23.sp,
                    textAlign = TextAlign.Center
                )

                BackpackItemGrid(
                    items = listOf(
                        player.backpack0Id,
                        player.backpack1Id,
                        player.backpack2Id,
                    )
                        // .map { itemId -> if (itemId == null) -1 else (itemId as BigDecimal).toShort() })
                        .map { itemId -> (itemId as BigDecimal?)?.toShort() ?: -1 },
                    modifier = Modifier.fillMaxWidth(fraction = Constants.HALF)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.33f)
                        .weight(1f)
                ) {
                    TextLabelRounded(
                        text = "Level: ${player.level}",
                        backgroundColor = PurePinkBackground,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (player.networth != null) {
                        TextLabelWithPictureRounded(
                            picture = MONEY_PICTURE,
                            backgroundColor = PurePinkBackground,
                            text = "${player.networth}",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.33f)
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    TextLabelWithTwoRows(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = PurePinkBackground,
                        label = KILLS_DEATH_ASSISTS,
                        text = getKillsDeathsAssists(player)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.33f)
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    TextLabelWithTwoRows(
                        modifier = Modifier.fillMaxWidth(),
                        label = GPM_XPM,
                        backgroundColor = PurePinkBackground,
                        text = getGpmXpm(player)
                    )
                }
            }
            if (player.stats?.healCount != null && player.stats.heroDamageCount != null &&
                player.stats.towerDamageCount != null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextLabelWithPictureRounded(
                        picture = SWORD,
                        text = "${player.stats.heroDamageCount}",
                        backgroundColor = PurePinkBackground,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.33f)
                            .weight(1f)
                    )
                    TextLabelWithPictureRounded(
                        picture = HEAL,
                        text = "${player.stats.healCount}",
                        backgroundColor = PurePinkBackground,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.33f)
                            .weight(1f)
                    )
                    TextLabelWithPictureRounded(
                        picture = TOWER,
                        text = "${player.stats.towerDamageCount}",
                        backgroundColor = PurePinkBackground,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.33f)
                            .weight(1f)
                    )
                }
            }
        }
    }
    