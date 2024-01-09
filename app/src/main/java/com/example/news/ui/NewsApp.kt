package com.example.news.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import com.example.news.NewsNavGraph
import com.example.news.R
import com.example.news.core.designsystem.component.NewsBackground
import com.example.news.core.designsystem.component.NewsGradientBackground
import com.example.news.core.designsystem.component.TopAppBar
import com.example.news.core.designsystem.icon.Icons
import com.example.news.core.designsystem.theme.LocalGradientColors
import com.example.news.utils.Categories
import com.example.news.utils.datastore.StoreCategorySelected
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewsApp(
    appState: NewsAppState = rememberNewsAppState()
) {
    NewsBackground {
        NewsGradientBackground(
            gradientColors = LocalGradientColors.current, // GradientColors()
        ) {
            val context = LocalContext.current
            val snackbarHostState = remember { SnackbarHostState() }

            val storeCategorySelected = StoreCategorySelected(context)
            val storeCategorySelectedState =
                storeCategorySelected.getCategorySelected.collectAsState(Categories.GENERAL.name)

            val categorySelected = storeCategorySelectedState.value
            var isShowBottomSheet by remember { mutableStateOf(false) }
            var isSearch by remember { mutableStateOf(false) }
            var searchQuery by remember { mutableStateOf("") }

            Scaffold(
                modifier = Modifier,
                /*topBar = {
                    TopAppBar(
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
                                            categorySelected.lowercase().replaceFirstChar {
                                                if (it.isLowerCase()) it.titlecase() else it.toString()
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
                                            Text(text = "Search in $categorySelected ..")
                                        },
                                        onValueChange = {
                                            searchQuery = it
                                        },
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Search,
                                        ),
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent,
                                            disabledContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent,
                                        ),
                                    )
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                isSearch = !isSearch
                                searchQuery = ""
                                homeViewModel.onEvent(HomeViewUiEvent.SearchArticle(search = ""))
                            }) {
                                Icon(
                                    painter = if (!isSearch) {
                                        painterResource(id = R.drawable.ic_search)
                                    } else {
                                        painterResource(id = R.drawable.ic_clear)
                                    },
                                    contentDescription = "Localized description",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                            IconButton(onClick = {

                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_more_vertical),
                                    contentDescription = "Localized description",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                },*/
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { padding ->
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(padding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
                        ),
                ) {
                    Column(Modifier.fillMaxSize()) {
                        TopAppBar(
                            titleRes = stringResource(
                                R.string.home_title,
                                categorySelected.lowercase()
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                            ),
                            navigationIcon = Icons.Search,
                            navigationIconContentDescription = stringResource(R.string.navigation),
                            actionIcon = Icons.MoreVertical,
                            actionIconContentDescription = stringResource(R.string.action_icon),
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Transparent,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            onNavigationClick = {
                                appState.navigateToSearch()
                            },
                            onActionClick = {
                                // TODO more / setting action
                            },
                        )

                        NewsNavGraph(
                            appState = appState,
                            onShowSnackbar = { message, action ->
                                snackbarHostState.showSnackbar(
                                    message = message,
                                    actionLabel = action,
                                    duration = SnackbarDuration.Short,
                                ) == SnackbarResult.ActionPerformed
                            },
                        )
                    }
                }
            }
        }
    }
}