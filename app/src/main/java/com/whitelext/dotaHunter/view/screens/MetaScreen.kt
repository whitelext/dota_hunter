package com.whitelext.dotaHunter.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.whitelext.dotaHunter.ui.theme.BackgroundDark
import com.whitelext.dotaHunter.ui.theme.BottomNavColor
import com.whitelext.dotaHunter.ui.theme.MatchField
import com.whitelext.dotaHunter.ui.theme.poppinsFamily
import com.whitelext.dotaHunter.util.Constants
import com.whitelext.dotaHunter.util.Utils
import com.whitelext.dotaHunter.viewModels.MetaViewModel
import com.whitelext.dotaHunter.viewModels.MetaViewModel.*

@ExperimentalCoilApi
@Composable
fun MetaScreen(
    metaViewModel: MetaViewModel = hiltViewModel()
) {
    val meta by metaViewModel.metaList.observeAsState()
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .background(color = BackgroundDark)
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(6.dp)
                .padding(top = 8.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = BottomNavColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(Constants.THIRTYTHIRD),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                text = "Hero",
                fontFamily = poppinsFamily,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(Constants.HALF)
                    .clickable {
                        metaViewModel.changeState(metaViewModel.CHANGE_FROM_PICK)
                        // scope.launch { scrollState.scrollTo(scrollState.value - (meta?.size ?: 0)) }
                    },
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                text = "PickRate",
                fontFamily = poppinsFamily,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        metaViewModel.changeState(metaViewModel.CHANGE_FROM_WIN)
                        // scope.launch { scrollState.scrollTo(scrollState.value - (meta?.size ?: 0)) }
                    },
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                text = "WinRate",
                fontFamily = poppinsFamily,
            )
        }

        LazyColumn(
            modifier = Modifier
                .padding(bottom = 56.dp)
                .scrollable(scrollState, Orientation.Vertical)
        ) {
            items(meta?.size ?: 0) { i ->
                ShowHero(
                    info = meta?.get(i),
                    viewModel = metaViewModel,
                )
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun ShowHero(
    info: HeroInfo?,
    viewModel: MetaViewModel
) {
    info?.let {

        val heroMap = viewModel.heroMap

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = MatchField),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text(heroMap[info.id.toString()]?.second ?: "Unknown hero")
            Box(
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberImagePainter(
                    data = Utils.getHeroUrl(
                        heroMap[it.id.toString()]?.shortName ?: "pudge"
                    ),
                    builder = {
                        crossfade(500)
                    }
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(Constants.THIRTYTHIRD)
                        .padding()
                        .padding(end = 2.dp)
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 10.dp,
                                topStart = 10.dp
                            )
                        )
                        .height(94.dp)
                )

                if (painter.state is ImagePainter.State.Loading) {
                    CircularProgressIndicator()
                }
            }

            Text(
                text = (1000.0 * it.picks.toDouble() / viewModel.picksSum).toString()
                    .substring(0..4) + "%",
                fontSize = 25.sp,
                modifier = Modifier.fillMaxWidth(Constants.HALF),
                textAlign = TextAlign.Center,
                fontFamily = poppinsFamily,
            )

            Text(
                text = (100.0 * it.wins.toDouble() / it.picks.toDouble()).toString()
                    .substring(0..4) + "%",
                fontSize = 25.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontFamily = poppinsFamily,
            )
        }
    }
}
