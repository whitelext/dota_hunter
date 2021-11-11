package com.whitelext.dotaHunter.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import com.whitelext.dotaHunter.ui.theme.Dota_hunterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Dota_hunterTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Home()
                }
            }
        }
    }

}

