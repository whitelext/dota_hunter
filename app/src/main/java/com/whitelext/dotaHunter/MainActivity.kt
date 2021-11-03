package com.whitelext.dotaHunter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.UserListQuery
import com.whitelext.dotaHunter.ui.theme.Dota_hunterTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels()
    private val getProfileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchViewModel.usersLiveData.observe(this, ::onUserListChanged)

        setContent {
            Dota_hunterTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    // Cache Test
                    var userInput by remember { mutableStateOf("") }
                    Column {
                        TextField(
                            value = userInput,
                            onValueChange = { newValue -> userInput = newValue })
                        Button(onClick = {
                            getProfileViewModel.onQueryChanged(userInput.toLong())
                        }) {
                            Text("Download")
                        }
                    }
                }
            }
        }
    }

    // searchViewModel.onQueryChanged here – for testing
    // TODO: set searchViewModel.onQueryChanged as listener for input text view and remove bottom
    override fun onResume() {
        super.onResume()
        searchViewModel.onQueryChanged(Random.nextInt(1, 1000).toString())
    }

    private fun onUserListChanged(newUserList: List<UserListQuery.Player>) {
        // TODO: update UI
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Dota_hunterTheme {
        Greeting("Android")
    }
}
