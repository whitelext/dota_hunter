package com.whitelext.dotaHunter.view.screens

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
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.UserListQuery
import com.whitelext.dotaHunter.ui.theme.BackgroundDark
import com.whitelext.dotaHunter.ui.theme.BottomNavColor
import com.whitelext.dotaHunter.ui.theme.PlayerField
import com.whitelext.dotaHunter.ui.theme.poppinsFamily
import com.whitelext.dotaHunter.util.Screen
import com.whitelext.dotaHunter.util.Utils
import com.whitelext.dotaHunter.view.CommonComponents.ProfilePhoto
import com.whitelext.dotaHunter.view.CommonComponents.Rank
import com.whitelext.dotaHunter.view.CommonComponents.TextLabelRounded
import com.whitelext.dotaHunter.viewModels.SearchViewModel
import java.math.BigDecimal

@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavController
) {
    val userInput by viewModel.userInput.observeAsState()
    val userList by viewModel.usersLiveData.observeAsState()

    Column(
        Modifier
            .background(BackgroundDark)
            .fillMaxWidth()
            .padding(bottom = 56.dp)
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
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
                    .weight(16f)
            ) {
                BasicTextField(
                    value = userInput ?: "",
                    onValueChange = { newValue ->
                        viewModel.userInput.value = newValue
//                        viewModel.onQueryChanged(userInput)
                    },
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .fillMaxWidth(),
                    maxLines = 1,
                    cursorBrush = SolidColor(MaterialTheme.colors.primary),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.subtitle1
                        .copy(
                            color = MaterialTheme.colors.onSurface,
                            fontFamily = poppinsFamily
                        ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.onQueryChanged(userInput ?: "")
                            keyboardController?.hide()
                        }
                    )
                )
            }
            Box(
                modifier = Modifier.weight(2f),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {
                        viewModel.clearInput()
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                }
            }
            Box(
                modifier = Modifier.weight(2f),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {
                        viewModel.onQueryChanged(userInput ?: "")
                        keyboardController?.hide()
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
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
