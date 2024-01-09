package com.example.news

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.news.feature.home.navigation.HOME_ROUTE
import com.example.news.feature.home.navigation.homeScreen
import com.example.news.feature.search.navigation.searchScreen
import com.example.news.ui.NewsAppState

@Composable
fun NewsNavGraph(
    appState: NewsAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen(
            onShowSnackbar = onShowSnackbar,
        )
        searchScreen(
            onBackClick = navController::popBackStack,
        )
    }
}