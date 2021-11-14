package com.whitelext.dotaHunter.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.UserListQuery
import com.whitelext.dotaHunter.SearchViewModel
import com.whitelext.dotaHunter.ui.theme.BackgroundDark
import com.whitelext.dotaHunter.ui.theme.BottomNavColor
import com.whitelext.dotaHunter.ui.theme.PlayerField
import com.whitelext.dotaHunter.ui.theme.poppinsFamily
import com.whitelext.dotaHunter.util.Screen
import com.whitelext.dotaHunter.util.Utils
import com.whitelext.dotaHunter.view.CommonComponents.ProfilePhoto
import com.whitelext.dotaHunter.view.CommonComponents.Rank
import com.whitelext.dotaHunter.view.CommonComponents.TextLabelRounded
import java.math.BigDecimal

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavController
) {
    var userInput by remember {
        mutableStateOf("")
    }
    val userList by viewModel.usersLiveData.observeAsState()

    Column(
        Modifier
            .background(BackgroundDark)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(7.dp)
                .padding(top = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(
                    color = BottomNavColor,
                    shape = MaterialTheme.shapes.large
                )
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 5.dp)
            ) {
                BasicTextField(
                    value = userInput,
                    onValueChange = { newValue ->
                        userInput = newValue
                        viewModel.onQueryChanged(userInput)
                    },
                    Modifier
                        .padding(start = 15.dp)
                        .fillMaxWidth(0.8f),
                    maxLines = 1,
                    cursorBrush = SolidColor(MaterialTheme.colors.primary),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.subtitle1
                        .copy(color = MaterialTheme.colors.onSurface),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.onQueryChanged(userInput)
                        }
                    )
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {
                        viewModel.onQueryChanged(userInput)
                    },
                    modifier = Modifier
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }
            }
        }
        ShowResult(userList ?: listOf(), navController)
    }
}

@Composable
private fun ShowResult(
    userList: List<UserListQuery.Player>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .background(color = BackgroundDark)
            .fillMaxHeight()
    ) {
        items(userList.size) { userIndex ->
            ShowPlayer(player = userList[userIndex], onClick = {
                navController.navigate(Screen.ProfileDetail.createRoute((userList[userIndex].id as BigDecimal).toLong()))
            })
        }
    }
}

@Composable
private fun ShowPlayer(
    player: UserListQuery.Player,
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
                ProfilePhoto(id = player.avatar)
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
                Rank(index = player.seasonRank.toString().toIntOrNull())
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
