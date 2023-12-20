package com.example.news

import androidx.navigation.NavHostController
import com.example.news.NewsScreens.HOME_SCREEN

private object NewsScreens {
    const val HOME_SCREEN = "home"
}

object NewsDestinationsArgs {

}

object NewsDestinations {
    const val HOME_ROUTE = HOME_SCREEN
}

class NewsNavigationActions(private val navController: NavHostController) {

}