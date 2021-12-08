package com.whitelext.dotaHunter.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.whitelext.dotaHunter.common.TimerType
import com.whitelext.dotaHunter.ui.theme.*
import com.whitelext.dotaHunter.viewModels.TimerViewModel

@Composable
fun TimerScreen(
    timerViewModel: TimerViewModel = hiltViewModel()
) {

    val aegisTimer by timerViewModel.aegisTimer.observeAsState()
    val timers by timerViewModel.timerCountdown.observeAsState()

    Column(
        modifier = Modifier
            .background(color = BackgroundDark)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(all = 8.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(color = MatchField)
                .clickable {
                    timerViewModel.startTimer(TimerType.AEGIS, 0)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Aegis",
                fontFamily = poppinsFamily,
                fontSize = 24.sp
            )
        }
        aegisTimer?.let {
            ShowTimer(
                remainingTime = it,
                timerType = TimerType.AEGIS,
                timerViewModel
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(all = 8.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(color = MatchField),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Buybacks",
                fontFamily = poppinsFamily,
                fontSize = 24.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            for (i in 0..4) {
                ShowBuyback(
                    remainingTime = timers?.get(i + 1) ?: "finished",
                    timerViewModel,
                    i + 1
                )
            }
        }
    }
}

@Composable
fun ShowBuyback(
    remainingTime: String,
    timerViewModel: TimerViewModel,
    id: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    ) {

        var isPressed by remember { mutableStateOf(false) }

        if (isPressed) {
            IconButton(
                onClick = {
                    timerViewModel.stopTimer(id)
                    isPressed = false
                },
                modifier = Modifier
                    .width(60.dp)
                    .height(55.dp)
                    .padding(3.dp)
                    .padding(start = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Clear,
                    contentDescription = null,
                    tint = Dire,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }
        } else {
            IconButton(
                onClick = {
                    timerViewModel.startTimer(TimerType.BUYBACK, id)
                    isPressed = true
                },
                modifier = Modifier
                    .width(60.dp)
                    .height(55.dp)
                    .padding(3.dp)
                    .padding(start = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = Dire,
                    modifier = Modifier
                        .padding(5.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(all = 8.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(color = Color.DarkGray)
            ) {
            }

            val x = if (remainingTime == "finished") 0f else remainingTime.substring(19).toFloat()
            Row(
                modifier = Modifier
                    .fillMaxWidth(x / TimerType.BUYBACK.length.toFloat())
                    .height(55.dp)
                    .padding(all = 8.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(color = Gold)
            ) {
            }
        }
    }
}

@Composable
fun ShowTimer(
    remainingTime: String,
    timerType: TimerType,
    timerViewModel: TimerViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(horizontal = 8.dp)
    ) {

        IconButton(
            onClick = {
                timerViewModel.stopTimer(0)
            },
            modifier = Modifier
                .width(60.dp)
                .height(55.dp)
                .padding(3.dp)
                .padding(start = 5.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Clear,
                contentDescription = null,
                tint = Dire,
                modifier = Modifier
                    .padding(5.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(all = 8.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(color = Color.DarkGray)
            ) {
            }

            val x = if (remainingTime == "finished") 0f else remainingTime.substring(19).toFloat()
            Row(
                modifier = Modifier
                    .fillMaxWidth(x / timerType.length.toFloat())
                    .height(55.dp)
                    .padding(all = 8.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(color = Lose)
            ) {
            }
        }
    }
}
