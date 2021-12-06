package com.whitelext.dotaHunter.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.whitelext.dotaHunter.FavoritesViewModel
import com.whitelext.dotaHunter.domain.model.FavoritePlayer
import com.whitelext.dotaHunter.ui.theme.BackgroundDark
import com.whitelext.dotaHunter.ui.theme.PlayerField
import com.whitelext.dotaHunter.ui.theme.poppinsFamily
import com.whitelext.dotaHunter.util.Converter
import com.whitelext.dotaHunter.util.Screen
import com.whitelext.dotaHunter.util.Utils
import com.whitelext.dotaHunter.view.CommonComponents.TextLabelRounded

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    navController: NavController
) {
    viewModel.initialize()

    Column(
        Modifier
            .background(BackgroundDark)
            .fillMaxWidth()
    ) {
        ShowFavorites(navController, viewModel)
    }
}

@Composable
private fun ShowFavorites(
    navController: NavController,
    viewModel: FavoritesViewModel
) {
    val favoriteList = viewModel.favoriteList.observeAsState()

    LazyColumn(
        modifier = Modifier
            .background(color = BackgroundDark)
            .fillMaxHeight()
    ) {
        favoriteList.value?.let {
            items(it.size) { userIndex ->
                ShowPlayer(
                    player = it[userIndex],
                    onClick = {
                        navController.navigate(
                            Screen.ProfileDetail.createRoute(
                                Converter.anyToId(
                                    it[userIndex].id
                                )!!
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ShowPlayer(
    player: FavoritePlayer,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(7.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = PlayerField)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column {
            Box {
                Image(
                    painter = rememberImagePainter(player.getAvatarBitmap()),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(15.dp)
                        .size(100.dp)
                        .clip(RoundedCornerShape(50))
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .horizontalScroll(state = rememberScrollState())
            ) {
                Box(
                    Modifier
                        .padding(6.dp)
                        .size(60.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(player.getRankBitmap()),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                }
                Text(
                    text = player.name ?: "ErrorName",
                    Modifier
                        .padding(vertical = 5.dp),
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            Row {
                TextLabelRounded(
                    text = Utils.getLastMatchDateTime(
                        player.lastMatchDateTime.toString().toLongOrNull()
                    )
                )
            }
        }
    }
}
