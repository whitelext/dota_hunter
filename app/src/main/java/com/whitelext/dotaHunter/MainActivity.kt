package com.whitelext.dotaHunter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.UserListQuery
import com.whitelext.dotaHunter.ui.theme.Dota_hunterTheme
import com.whitelext.dotaHunter.util.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels()
    private val itemsViewModel: ItemsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Dota_hunterTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Search(searchViewModel)
                }
            }
        }
    }

}

@Composable
private fun Search(
    viewModel: SearchViewModel
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