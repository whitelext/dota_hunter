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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.UserProfileQuery
import com.whitelext.dotaHunter.ProfileViewModel
import com.whitelext.dotaHunter.R
import com.whitelext.dotaHunter.domain.ItemStore
import com.whitelext.dotaHunter.ui.theme.*
import com.whitelext.dotaHunter.util.Constants.THIRTYTHIRD
import com.whitelext.dotaHunter.util.Utils
import com.whitelext.dotaHunter.view.CommonComponents.ProfilePhoto
import com.whitelext.dotaHunter.view.CommonComponents.Rank
import com.whitelext.dotaHunter.view.CommonComponents.TextLabelRounded
import java.math.BigDecimal

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    profileId: Long
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
            // TODO: implement onAddToFavoritesClickListener
            UserCard(player = player, onAddToFavoritesClickListener = { false })
            player.matches?.let { Matches(matches = player.matches.filterNotNull()) }
        }
    }
}

@Composable
private fun UserCard(
    player: UserProfileQuery.Player,
    onAddToFavoritesClickListener: (userId: Long) -> Boolean
) {
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
                    onClick = { onAddToFavoritesClickListener((player.steamAccount?.id as BigDecimal).toLong()) },
                    modifier = Modifier
                        .padding(end = 10.dp)
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
        }
    }
}

@Composable
private fun Matches(matches: List<UserProfileQuery.Match>) {
    LazyColumn {
        items(matches.size) { i -> ShowMatch(match = matches[i]) }
    }
}

@Composable
private fun ShowMatch(match: UserProfileQuery.Match) {
    match.players?.first()?.let {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = MatchField)
                .fillMaxWidth(),
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
                        text = Utils.convertUnixToDate(
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
                    text = Utils.getKillsDeathsAssists(it),
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

@Composable
private fun ItemsGrid(items: List<Short>) {

    Column {
        Row {
            for (i in 0..2) {
                val itemName = ItemStore.getItemById(items[i].toInt())
                if (items[i] < 0 || itemName == null) EmptySpace() else ItemIcon(
                    itemUrl = Utils.getItemUrl(itemName),
                    itemName = itemName
                )
            }
        }
        Row {
            for (i in 3..5) {
                val itemName = ItemStore.getItemById(items[i].toInt())
                if (items[i] < 0 || itemName == null) EmptySpace() else ItemIcon(
                    itemUrl = Utils.getItemUrl(itemName),
                    itemName = itemName
                )
            }
        }
    }
}

@Composable
private fun ItemIcon(itemUrl: String, itemName: String) {
    Image(
        painter = rememberImagePainter(itemUrl),
        contentDescription = itemName,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(horizontal = 3.dp, vertical = 2.dp)
            .width(55.dp)
            .height(45.dp)
            .clip(RoundedCornerShape(10.dp))
    )
}

@Composable
fun EmptySpace() {
    Spacer(
        modifier = Modifier
            .padding(horizontal = 3.dp, vertical = 2.dp)
            .width(55.dp)
            .height(45.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = BackgroundDark,
                shape = RoundedCornerShape(10.dp)
            )
    )
}
