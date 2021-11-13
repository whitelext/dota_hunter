package com.whitelext.dotaHunter.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.whitelext.dotaHunter.util.Utils
import com.whitelext.dotaHunter.view.CommonComponents.ProfilePhoto
import com.whitelext.dotaHunter.view.CommonComponents.Rank
import com.whitelext.dotaHunter.view.CommonComponents.TextLabelOval
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
        player?.let {
            // TODO: implement onAddToFavoritesClickListener
            UserCard(player = player!!, onAddToFavoritesClickListener = { false })
            player!!.matches?.let { Matches(matches = player!!.matches!!.filterNotNull()) }
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
            .padding(top = 8.dp, bottom = 4.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = PlayerField)
            .fillMaxWidth()
    ) {
        Column {
            ProfilePhoto(id = player?.steamAccount?.avatar)
        }
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Rank(index = player.steamAccount?.seasonRank.toString().toIntOrNull())
                Text(
                    text = player?.steamAccount?.name ?: stringResource(R.string.loading_text),
                    Modifier
                        .padding(vertical = 5.dp)
                        .fillMaxWidth(0.9f),
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                ) {
                if (player != null) {
                    TextLabelOval(
                        text = stringResource(
                            R.string.game_count,
                            player.matchCount ?: 0
                        )
                    )
                    TextLabelOval(text = Utils.getWinRate(player))
                    IconButton(
                        onClick = { onAddToFavoritesClickListener((player.steamAccount?.id as BigDecimal).toLong()) },
                        modifier = Modifier.padding(end = 10.dp).size(75.dp)
                    ) {
                        Icon(
                            // TODO: if (player in favorites) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder
                            imageVector = Icons.Rounded.FavoriteBorder,
                            contentDescription = stringResource(R.string.add_to_favorites_label),
                            tint = MaterialTheme.colors.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Matches(matches: List<UserProfileQuery.Match>) {
    LazyColumn {
        items(matches.size) { i -> Match(matches[i]) }
    }
}

@Composable
private fun Match(match: UserProfileQuery.Match) {
    match.players?.first()?.let {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = MatchField)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.33f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberImagePainter(Utils.getHeroUrl(it.hero?.displayName)),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(15.dp)
                        .size(100.dp)
                )
                it.isVictory?.let { isVictory ->
                    TextLabelOval(
                        text = stringResource(id = if (isVictory) R.string.win_match else R.string.lose_match),
                        textColor = if (isVictory) GreenDark else RedDark,
                        backgroundColor = if (isVictory) GreenLight else RedLight,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    ItemsGrid(items = listOf(it.item0Id, it.item1Id, it.item2Id, it.item3Id, it.item4Id, it.item5Id)
                        .map { itemId -> if (itemId == null) -1 else (itemId as BigDecimal).toShort() })
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    TextLabelOval(Utils.getKillsAssistsDeaths(it), modifier = Modifier.fillMaxWidth(0.5f))
                    match.durationSeconds?.let { TextLabelOval(Utils.getDuration(match.durationSeconds), modifier = Modifier.fillMaxWidth()) }
                }

            }
        }
    }
}

@Composable
private fun ItemsGrid(items: List<Short>) {

    Column (modifier = Modifier.padding(end = 7.dp, top = 16.dp, bottom = 4.dp)){
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
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .width(65.dp)
            .height(55.dp)
            .clip(RoundedCornerShape(40))
    )

}

@Composable fun EmptySpace() {
    Spacer(
        modifier = Modifier
            .padding(top = 4.dp)
            .padding(horizontal = 3.dp)
            .width(65.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(30))
            .border(
                width = 1.dp,
                color = BackgroundDark,
                shape = RoundedCornerShape(30)
            )
    )

}
