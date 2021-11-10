package com.whitelext.dotaHunter.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.UserListQuery
import com.whitelext.dotaHunter.SearchViewModel
import com.whitelext.dotaHunter.util.Utils

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    var userInput by remember {
        mutableStateOf("")
    }
    val userList by viewModel.usersLiveData.observeAsState()

    Column {
        Row {
            OutlinedTextField(
                value = userInput,
                onValueChange = { newValue ->
                    userInput = newValue
                },
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.onQueryChanged(userInput)
                })
            )
            OutlinedButton(
                onClick = {
                    viewModel.onQueryChanged(userInput)
                }
            ) {
                Text("Search")
            }
        }
        ShowResult(userList ?: listOf())
    }
}

@Composable
private fun ShowResult(
    userList: List<UserListQuery.Player>
) {
    LazyColumn {
        itemsIndexed(userList) { index, user ->
            Rank(index = index)
            ShowPlayer(player = user)
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
    ) {
        Text(player.name ?: "ErrorName")
    }
}

@Composable
private fun Rank(index: Int) {
    Box {
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