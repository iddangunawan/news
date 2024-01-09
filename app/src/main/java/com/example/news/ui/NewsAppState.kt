package com.example.news.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.news.feature.search.navigation.navigateToSearch
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberNewsAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): NewsAppState {
    return remember(
        navController,
        coroutineScope,
    ) {
        NewsAppState(
            navController,
            coroutineScope,
        )
    }
}

@Stable
class NewsAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
) {
    fun navigateToSearch() = navController.navigateToSearch()
}