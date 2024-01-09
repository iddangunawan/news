package com.example.news.feature.home

import android.content.Context
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.news.R
import com.example.news.core.designsystem.component.LoadingRipple
import com.example.news.domain.model.Article
import com.example.news.ui.component.ErrorButton
import com.example.news.utils.Categories
import com.example.news.utils.Const
import com.example.news.utils.datastore.StoreCategorySelected
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val articleListState by homeViewModel.articleListState.collectAsState()
    val articleListResponse = articleListState.articleList?.collectAsLazyPagingItems()
    val storeCategorySelected = StoreCategorySelected(context)
    val storeCategorySelectedState =
        storeCategorySelected.getCategorySelected.collectAsState(Categories.GENERAL.name)

    HomeScreen(
        onShowSnackbar = onShowSnackbar,
        articleList = articleListResponse,
        storeCategorySelected = storeCategorySelected,
        storeCategorySelectedState = storeCategorySelectedState,
        homeViewModel = homeViewModel,
        onClickSource = {
            // TODO browse new by source
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    articleList: LazyPagingItems<Article>? = null,
    storeCategorySelected: StoreCategorySelected,
    storeCategorySelectedState: State<String>,
    homeViewModel: HomeViewModel,
    onClickSource: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    val categorySelected = storeCategorySelectedState.value
    var isShowBottomSheet by remember { mutableStateOf(false) }
    val searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(categorySelected) {
        homeViewModel.onEvent(HomeViewUiEvent.CategoryArticle(category = categorySelected))
    }

    LaunchedEffect(searchQuery) {
        delay(2.seconds)
        homeViewModel.onEvent(HomeViewUiEvent.SearchArticle(search = searchQuery))
    }

    Column(modifier = modifier) {
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(Categories.entries.toTypedArray()) { category ->
                SuggestionChip(
                    onClick = {
                        scope.launch {
                            storeCategorySelected.saveCategorySelected(category.name)
                        }
                    },
                    label = {
                        Text(
                            text = category.name.lowercase().replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase() else it.toString()
                            }
                        )
                    },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (category.name == categorySelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Transparent
                        },
                        labelColor = if (category.name == categorySelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        borderColor = MaterialTheme.colorScheme.primary,
                        borderWidth = 1.dp,
                    ),
                )
            }
        }
        if (articleList == null) return
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(articleList.itemCount) { index ->
                val context = LocalContext.current
                val backgroundColor = MaterialTheme.colorScheme.background.toArgb()

                val author = articleList[index]?.author ?: "-"
                val title = articleList[index]?.title ?: "-"

                val annotatedString = buildAnnotatedString {
                    withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
                        append("$author ")
                    }
                    withStyle(style = MaterialTheme.typography.bodyMedium.toSpanStyle()) {
                        append(title)
                    }
                }

                Card(
                    onClick = {
                        launchCustomChromeTab(
                            context,
                            Uri.parse(articleList[index]?.url),
                            backgroundColor
                        )
                    },
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    Column {
                        ArticleImage(
                            if (!articleList[index]?.urlToImage.isNullOrEmpty()) {
                                articleList[index]?.urlToImage
                            } else {
                                Const.DEFAULT_IMAGE
                            }
                        )
                        Box(modifier = Modifier.padding(8.dp)) {
                            Column {
                                Text(
                                    annotatedString,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Text(
                                        "${dateFormat(articleList[index]?.publishedAt ?: " - ")} • ",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                    Text(
                                        "Source: ${articleList[index]?.source?.name ?: "-"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.clickable {
                                            articleList[index]?.source?.name.let {
                                                onClickSource.invoke(articleList[index]?.source?.name.toString())
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
            articleList.apply {
                item {
                    when {
                        loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                            LoadingRipple(
                                modifier = Modifier.fillParentMaxSize(),
                                circleColor = MaterialTheme.colorScheme.primary,
                            )
                        }

                        loadState.refresh is LoadState.Error || loadState.append is LoadState.Error -> {
                            val errorMessage =
                                (articleList.loadState.refresh as? LoadState.Error)?.error?.message
                                    ?: (articleList.loadState.append as? LoadState.Error)?.error?.message
                                    ?: "Unknown error"
                            ErrorButton(
                                modifier = Modifier.fillParentMaxSize(),
                                message = errorMessage,
                                btnText = "Try again!",
                                onClick = {
                                    retry()
                                }
                            )
                        }

                        else -> {
                            when {
                                articleList.itemSnapshotList.isEmpty() -> {
                                    Column(
                                        modifier = Modifier.fillParentMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Search,
                                            contentDescription = "Empty Result",
                                            modifier = Modifier.size(60.dp),
                                        )
                                        Text(
                                            "Data not found !",
                                            style = MaterialTheme.typography.headlineMedium,
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (isShowBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                isShowBottomSheet = false
            },
            sheetState = sheetState,
        ) {
            Categories.list.forEach { category ->
                Box(
                    modifier = Modifier
                        .clickable {
                            scope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        scope.launch {
                                            storeCategorySelected.saveCategorySelected(category.name)
                                        }
                                        isShowBottomSheet = false
                                    }
                                }
                        }
                        .fillMaxWidth()
                        .background(if (category.name == categorySelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = category.name,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleImage(imageUrl: String?) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        model = imageUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )
    val isLocalInspection = LocalInspectionMode.current
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            // Display a progress bar while loading
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 180.dp),
            contentScale = ContentScale.Crop,
            painter = if (isError.not() && !isLocalInspection) {
                imageLoader
            } else {
                painterResource(R.drawable.ic_placeholder_default)
            },
            contentDescription = "Article Image",
        )
    }
}

fun dateFormat(inputDateString: String): String {
    // Parse the input date string
    val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
    val parsedDateTime = LocalDateTime.parse(inputDateString, inputFormatter)
    val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    return parsedDateTime.format(outputFormatter)
}

fun launchCustomChromeTab(context: Context, uri: Uri, @ColorInt toolbarColor: Int) {
    val customTabBarColor = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(toolbarColor).build()
    val customTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(customTabBarColor)
        .build()

    customTabsIntent.launchUrl(context, uri)
}