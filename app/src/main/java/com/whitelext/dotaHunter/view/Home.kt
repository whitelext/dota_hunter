package com.whitelext.dotaHunter.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.whitelext.dotaHunter.ui.theme.BottomNavColor
import com.whitelext.dotaHunter.util.Constants
import com.whitelext.dotaHunter.util.Screen
import com.whitelext.dotaHunter.view.screens.*
import kotlinx.coroutines.FlowPreview

@ExperimentalCoilApi
@ExperimentalComposeUiApi
@FlowPreview
@Composable
fun Home() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            val currentRoute = currentRoute(navController = navController)
            if (currentRoute != Screen.ProfileDetail.route &&
                currentRoute != Screen.MatchDetail.route
            ) BottomNavigationBar(
                controller = navController,
                onNavigationSelected = { screen ->
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselect the same item
                        launchSingleTop = true
                        // Restore state when reselect a previously selected item
                        restoreState = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) {
        // Navigation(navController = navController, modifier = Modifier.padding(innerPadding))
        Navigation(navController = navController, modifier = Modifier)
    }
}

@ExperimentalCoilApi
@ExperimentalComposeUiApi
@FlowPreview
@Composable
fun Navigation(navController: NavHostController, modifier: Modifier) {

    NavHost(navController, startDestination = Screen.Search.route, modifier = modifier) {
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(navController = navController)
        }
        composable(
            route = Screen.ProfileDetail.route,
            arguments = listOf(navArgument(Constants.PROFILE_ID) { type = NavType.LongType })
        ) {
            val profileId = it.arguments?.getLong(Constants.PROFILE_ID) ?: 0L
            ProfileScreen(profileId = profileId, navController = navController)
        }

        composable(Screen.Meta.route) {
            MetaScreen()
        }

        composable(Screen.Timer.route) {
            TimerScreen()
        }

        composable(
            route = Screen.MatchDetail.route,
            arguments = listOf(navArgument(Constants.MATCH_ID) { type = NavType.LongType })
        ) {
            val matchId = it.arguments?.getLong(Constants.MATCH_ID) ?: 0L
            MatchScreen(matchId = matchId, navController = navController)
        }
    }
}

@Composable
fun BottomNavigationBar(
    controller: NavHostController,
    onNavigationSelected: (Screen) -> Unit,
    modifier: Modifier
) {
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(
        backgroundColor = BottomNavColor,
        elevation = 8.dp,
        modifier = modifier
    ) {
        HomeNavigationItems.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = screen.icon),
                        contentDescription = screen.title
                    )
                },
                label = { Text(text = screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Black,
                onClick = { onNavigationSelected(screen) },
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

private val HomeNavigationItems = listOf(
    Screen.Search,
    Screen.Favorites,
    Screen.Meta,
    Screen.Timer
)
