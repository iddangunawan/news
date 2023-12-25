package com.example.news.fragment.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.news.fragment.home.screen.HomeScreen
import com.example.news.viewmodel.HomeViewModel
import com.example.news.viewmodel.HomeViewUiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFragment(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val filterListApkUiState by homeViewModel.filterArticleListState.collectAsState()
    val articleListState by homeViewModel.articleListState.collectAsState()
    val articleListResponse = articleListState.articleList?.collectAsLazyPagingItems()

    val categories =
        listOf("business", "entertainment", "general", "health", "science", "sports", "technology")
    var categorySelected by rememberSaveable { mutableStateOf(categories[6]) }
    var isShowBottomSheet by remember { mutableStateOf(false) }

    var isSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(categorySelected) {
        homeViewModel.onEvent(HomeViewUiEvent.CategoryArticle(category = categorySelected))
    }

    LaunchedEffect(searchQuery) {
        delay(2.seconds)
        homeViewModel.onEvent(HomeViewUiEvent.SearchArticle(search = searchQuery))
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!isSearch) {
                            Text(
                                "Now in ${
                                    categorySelected.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ROOT
                                        ) else it.toString()
                                    }
                                }",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable {
                                    isShowBottomSheet = true
                                },
                            )
                            IconButton(onClick = {
                                isShowBottomSheet = true
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "Change category"
                                )
                            }
                        } else {
                            TextField(
                                value = searchQuery,
                                placeholder = {
                                    Text(text = "Search ..")
                                },
                                onValueChange = {
                                    searchQuery = it
                                },
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isSearch = !isSearch
                    }) {
                        Icon(
                            imageVector = if (!isSearch) Icons.Filled.Search else Icons.Filled.Clear,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        HomeScreen(
            modifier = Modifier.padding(it),
            articleList = articleListResponse,
        )
        if (isShowBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    isShowBottomSheet = false
                },
                sheetState = sheetState,
                windowInsets = WindowInsets(bottom = 0),
            ) {
                categories.forEach { category ->
                    Box(
                        modifier = Modifier
                            .clickable {
                                scope
                                    .launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible && category != categorySelected) {
                                            categorySelected = category
                                            isShowBottomSheet = false
                                        }
                                    }
                            }
                            .fillMaxWidth()
//                            .background(if (category == categorySelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = category.uppercase(),
                            maxLines = 1,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}