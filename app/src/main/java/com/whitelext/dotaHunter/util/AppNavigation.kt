package com.whitelext.dotaHunter.util

import com.whitelext.dotaHunter.R

sealed class Screen(var route: String, var icon: Int, var title: String) {
    object Search : Screen("search", R.drawable.ic_search_24, "Search")
    object Favorites : Screen("favorites", R.drawable.ic_favorites_24, "Favorites")
    object ProfileDetail : AppNavigation("profile/{profileId}") {
        fun createRoute(profileId: Long) = "profile/$profileId"
    }

    object Meta : Screen("meta", R.drawable.ic_baseline_local_fire_department_24, "Meta")

    object MatchDetail : AppNavigation("match/{matchId}") {
        fun createRoute(matchId: Long) = "match/$matchId"
    }
}

sealed class AppNavigation(val route: String)
