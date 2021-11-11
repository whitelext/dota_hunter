package com.whitelext.dotaHunter.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import coil.compose.rememberImagePainter
import com.example.UserListQuery
import com.whitelext.dotaHunter.SearchViewModel
import com.whitelext.dotaHunter.ui.theme.BackgroundDark
import com.whitelext.dotaHunter.ui.theme.BottomNavColor
import com.whitelext.dotaHunter.ui.theme.PlayerField
import com.whitelext.dotaHunter.ui.theme.poppinsFamily
import com.whitelext.dotaHunter.util.Utils

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
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
                    },
                    Modifier.padding(start = 15.dp),
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
        ShowResult(userList ?: listOf())
    }
}

@Composable
private fun ShowResult(
    userList: List<UserListQuery.Player>
) {
    LazyColumn(
        modifier = Modifier
            .background(color = BackgroundDark)
            .fillMaxHeight()
    ) {
        items(userList.size) { user ->
            ShowPlayer(player = userList[user])
        }
    }
}

@Composable
private fun ShowPlayer(
    player: UserListQuery.Player,
    //onClick: () -> Unit
) {
    Row(
        //modifier = Modifier.clickable { onClick() }
        modifier = Modifier
            .padding(7.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = PlayerField)
            .fillMaxWidth()
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
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Rank(index = player.seasonRank.toString().toIntOrNull())
                Text(
                    text = player.name ?: "ErrorName",
                    Modifier.padding(vertical = 5.dp),
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            Row {
                LastTimeOnline(count = player.lastMatchDateTime.toString().toLongOrNull())
            }
        }
    }
}

@Composable
private fun Rank(index: Int?) {
    Box(
        Modifier
            .padding(6.dp)
            .size(60.dp)
    ) {
        Image(
            painter = rememberImagePainter(
                Utils.getRankUrl(
                    index
                )
            ),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Image(
            painter = rememberImagePainter(
                Utils.getStarsUrl(
                    index
                )
            ),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
private fun ProfilePhoto(id: String?) {
    Image(
        painter = rememberImagePainter(
            Utils.getAvatarUrl(
                id
            )
        ),
        contentDescription = null,
        modifier = Modifier
            .padding(15.dp)
            .size(100.dp)
            .clip(RoundedCornerShape(50))
    )
}

@Composable
private fun LastTimeOnline(count: Long?) {
    Text(
        text = Utils.getLastMatchDateTime(count),
        modifier = Modifier
            .padding(8.dp)
            .padding(bottom = 7.dp)
            .border(
                width = 2.dp,
                color = BackgroundDark,
                shape = RoundedCornerShape(55.dp)
            )
            .padding(8.dp),
        color = BackgroundDark,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    )
}