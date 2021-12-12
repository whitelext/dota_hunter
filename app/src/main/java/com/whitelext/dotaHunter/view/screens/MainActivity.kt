package com.whitelext.dotaHunter.view.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import coil.annotation.ExperimentalCoilApi
import com.whitelext.dotaHunter.ui.theme.Dota_hunterTheme
import com.whitelext.dotaHunter.view.Home
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalCoilApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            Dota_hunterTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Home()
                }
            }
        }
    }
}
