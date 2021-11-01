package com.whitelext.dotaHunter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.UserListQuery
import com.whitelext.dotaHunter.ui.theme.Dota_hunterTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchViewModel.usersLiveData.observe(this, ::onUserListChanged)

        setContent {
            Dota_hunterTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
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
