package com.example.news.fragment.home.screen

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.news.R
import com.example.news.domain.model.Article
import com.example.news.ui.component.ErrorButton
import com.example.news.ui.component.LoadingCircular
import com.example.news.utils.Const
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    articleList: LazyPagingItems<Article>? = null,
) {
    if (articleList == null) return
    LazyColumn(modifier = modifier) {
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
                        LoadingCircular(modifier = Modifier.fillMaxWidth())
                    }

                    loadState.refresh is LoadState.Error || loadState.append is LoadState.Error -> {
                        ErrorButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Error get data ..",
                            onClick = {
                                retry()
                            }
                        )
                    }

                    else -> {
                        when {
                            articleList.itemSnapshotList.isEmpty() -> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "Empty Result",
                                        modifier = Modifier.size(60.dp),
                                    )
                                    Text(
                                        "Search not found !",
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

@RequiresApi(Build.VERSION_CODES.O)
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