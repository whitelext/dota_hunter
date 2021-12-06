package com.whitelext.dotaHunter.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.UserProfileQuery
import com.whitelext.dotaHunter.ProfileViewModel
import com.whitelext.dotaHunter.R
import com.whitelext.dotaHunter.ui.theme.*
import com.whitelext.dotaHunter.util.Constants.THIRTYTHIRD
import com.whitelext.dotaHunter.util.Converter
import com.whitelext.dotaHunter.util.Screen
import com.whitelext.dotaHunter.util.Utils
import com.whitelext.dotaHunter.view.CommonComponents.ItemsGrid
import com.whitelext.dotaHunter.view.CommonComponents.ProfilePhoto
import com.whitelext.dotaHunter.view.CommonComponents.Rank
import com.whitelext.dotaHunter.view.CommonComponents.TextLabelRounded
import java.math.BigDecimal

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    profileId: Long,
    navController: NavController
) {

    val player by profileViewModel.profileData.observeAsState()

    profileViewModel.initUser(profileId)

    Column(
        Modifier
            .background(BackgroundDark)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        player?.let { player ->
            UserCard(player = player, profileViewModel, onFavoritesClickListener = {
                profileViewModel.changeFavorite()
            })
            player.matches?.let { Matches(matches = player.matches.filterNotNull(), navController) }
        }
    }

}

@Composable
private fun UserCard(
    player: UserProfileQuery.Player,
    viewModel: ProfileViewModel,
    onFavoritesClickListener: () -> Unit
) {

    val isFavorite by viewModel.isFavorite.observeAsState(false)

    Row(
        modifier = Modifier
            .padding(7.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = PlayerField)
            .fillMaxWidth()
            .height(135.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            ProfilePhoto(id = player.steamAccount?.avatar)
        }
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            if (player.matchCount == null || player.winCount == null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(end = 4.dp)
                ) {
                    Rank(index = player.steamAccount?.seasonRank.toString().toIntOrNull())
                    Text(
                        text = player.steamAccount?.name ?: stringResource(R.string.loading_text),
                        Modifier
                            .padding(vertical = 5.dp)
                            .fillMaxWidth(0.75f)
                            .horizontalScroll(state = rememberScrollState(0)),
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp
                    )
                    IconButton(
                        onClick = { onFavoritesClickListener() },
                        modifier = Modifier
                            .size(75.dp)
                            .weight(1f)

                    ) {
                        Icon(
                            // if (player in favorites) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder
                            imageVector = Icons.Rounded.FavoriteBorder,
                            contentDescription = stringResource(R.string.add_to_favorites_label),
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .horizontalScroll(state = rememberScrollState(0))
                ) {
                    Rank(index = player.steamAccount?.seasonRank.toString().toIntOrNull())
                    Text(
                        text = player.steamAccount?.name ?: stringResource(R.string.loading_text),
                        Modifier
                            .padding(vertical = 5.dp)
                            .fillMaxWidth(0.9f),
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    TextLabelRounded(
                        text = stringResource(
                            R.string.game_count,
                            player.matchCount ?: 0,
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    TextLabelRounded(
                        text = Utils.getWinRate(player),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onFavoritesClickListener() },
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(75.dp)
                            .weight(1f)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = stringResource(R.string.add_to_favorites_label),
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Matches(
    matches: List<UserProfileQuery.Match>,
    navController: NavController
) {
    if (matches.isEmpty()) {
        DrawLock()
    } else {
        LazyColumn {
            items(matches.size) { i ->
                ShowMatch(
                    match = matches[i],
                    onClick = {
                        navController.navigate(Screen.MatchDetail.createRoute((matches[i].id as BigDecimal).toLong()))
                    }
                )
            }
        }
    }
}

@Composable
fun DrawLock() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberImagePainter(Utils.getLockUrl()),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding()
                .padding(end = 2.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(fraction = THIRTYTHIRD)
                .align(alignment = Alignment.Center)
        )
    }
}

@Composable
private fun ShowMatch(
    match: UserProfileQuery.Match,
    onClick: () -> Unit
) {
    match.players?.first()?.let {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = MatchField)
                .fillMaxWidth()
                .clickable { onClick() },
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .padding(horizontal = 5.dp)
            ) {
                Image(
                    painter = rememberImagePainter(Utils.getHeroUrl(it.hero?.shortName)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding()
                        .padding(end = 2.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .weight(1f)
                        .fillMaxWidth(fraction = THIRTYTHIRD)
                        .height(94.dp)
                        .align(Alignment.CenterVertically)
                )

                ItemsGrid(
                    items = listOf(
                        it.item0Id,
                        it.item1Id,
                        it.item2Id,
                        it.item3Id,
                        it.item4Id,
                        it.item5Id
                    )
                        // .map { itemId -> if (itemId == null) -1 else (itemId as BigDecimal).toShort() })
                        .map { itemId -> (itemId as BigDecimal?)?.toShort() ?: -1 }
                )
            }

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                it.isVictory?.let { isVictory ->
                    TextLabelRounded(
                        text = Converter.unixToDate(
                            match.startDateTime.toString().toLongOrNull()
                        ),
                        textColor = if (isVictory) Win else Lose,
                        backgroundColor = MatchField,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.ExtraBold,
                        borderWidth = 5.dp
                    )
                }
                TextLabelRounded(
                    text = Utils.getKillsDeathsAssistsProfile(it),
                    modifier = Modifier.weight(1f)
                )
                match.durationSeconds?.let {
                    TextLabelRounded(
                        text = Utils.getDuration(match.durationSeconds),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
