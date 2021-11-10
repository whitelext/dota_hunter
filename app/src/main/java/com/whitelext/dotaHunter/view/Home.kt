package com.whitelext.dotaHunter.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.whitelext.dotaHunter.util.Screen
import kotlinx.coroutines.FlowPreview

@FlowPreview
@Composable
fun Home() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
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
        //Navigation(navController = navController, modifier = Modifier.padding(innerPadding))
        Navigation(navController = navController, modifier = Modifier)
    }
}

@FlowPreview
@Composable
fun Navigation(navController: NavHostController, modifier: Modifier) {

    NavHost(navController, startDestination = Screen.Search.route, modifier = modifier) {
        composable(Screen.Search.route) {
            SearchScreen()
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen()
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
        backgroundColor = MaterialTheme.colors.surface,
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
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.LightGray,
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
    Screen.Favorites
)