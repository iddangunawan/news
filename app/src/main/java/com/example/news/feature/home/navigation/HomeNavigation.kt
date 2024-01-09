package com.example.news.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.news.feature.home.HomeRoute

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) = navigate(HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(
            onShowSnackbar = onShowSnackbar
        )
    }
}